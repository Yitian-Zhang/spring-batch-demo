package com.yitian.examples.springbatchdemo.demo1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yitian.examples.springbatchdemo.demo1.BlogInfoValidator;
import com.yitian.examples.springbatchdemo.demo1.BlogInfoItemProcessor;
import com.yitian.examples.springbatchdemo.demo1.BlogInfoFileReaderListener;
import com.yitian.examples.springbatchdemo.demo1.BlogInfoFileWriterListener;
import com.yitian.examples.springbatchdemo.demo1.BlogInfoJobListener;
import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

/**
 * BlogInfo处理示例
 * JobRepository和JobLanucher的设置见 @com.yitian.examples.springbatchdemo.config.BatchCommonConfig
 *
 * @author zhangyitian
 * Created by yitian.zyt on 2021/7/11
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class BlogInfoBatchConfig {

    /**
     * Job配置
     *
     * @param jobBuilderFactory     自动注册的JobBuilderFactory对象，用于创建Job
     * @param blogInfoStep          自定义处理step
     * @return                      Job
     */
    @Bean
    public Job blogInfoJob(JobBuilderFactory jobBuilderFactory, Step blogInfoStep) {
        return jobBuilderFactory.get("BlogInfo-SimpledDBJob1")
                .incrementer(new RunIdIncrementer())
                .start(blogInfoStep)
                .listener(blogInfoJobListener())
                .build();
    }

    /**
     * JobListener配置
     *
     * @return  JobListener
     */
    @Bean
    public Object blogInfoJobListener() {
        return new BlogInfoJobListener();
    }

    /**
     * 定义Step：包括itemReader、Processor和Writer的配置
     *
     * @param stepBuilderFactory    StepBuilder工厂
     * @param blogInfoFileReader    ItemReader
     * @param blogInfoFileWriter    ItemWriter
     * @param blogInfoProcessor     ItemProcessor
     * @return                      Step
     */
    @Bean
    public Step blogInfoStep(StepBuilderFactory stepBuilderFactory, ItemReader<BlogInfo> blogInfoFileReader,
                             ItemProcessor<BlogInfo, BlogInfo> blogInfoProcessor, ItemWriter<BlogInfo> blogInfoFileWriter,
                             ItemWriter<BlogInfo> blogInfoDBWriter) {
        return stepBuilderFactory.get("BlogInfo-Step")
                .<BlogInfo, BlogInfo> chunk(5)
                .reader(blogInfoFileReader).faultTolerant().retryLimit(3).retry(Exception.class).skip(Exception.class).skipLimit(2)
                .listener(new BlogInfoFileReaderListener())
                .processor(blogInfoProcessor)
                // 输入结果写入DB，如需写入文件使用blogInfoFileWriter
                .writer(blogInfoDBWriter).faultTolerant().skip(Exception.class).skipLimit(2)
                .listener(new BlogInfoFileWriterListener())
                .build();
    }

    /**
     * ItemReader定义：读取文件数据+entity实体类映射
     *
     * @return ItemReader
     */
    @Bean
    public ItemReader<BlogInfo> blogInfoFileReader() {
        // 使用FlatFileItemReader去读cvs文件，一行即一条数据
        FlatFileItemReader<BlogInfo> reader = new FlatFileItemReader<>();
        // 设置文件所处路径
        reader.setResource(new ClassPathResource("static/bloginfo.csv"));
        // entity和csv数据映射
        reader.setLineMapper(new DefaultLineMapper<BlogInfo>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setDelimiter(",");
                    }
                    {
                        setNames("blogId", "blogTitle", "blogUrl", "blogAuthor", "createTime", "status", "blogItem");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<BlogInfo>() {
                    {
                        setTargetType(BlogInfo.class);
                    }
                });
            }
        });
        return reader;
    }

    /**
     * 注册ItemProcessor: 处理数据+校验数据
     *
     * @return ItemProcessor
     */
    @Bean
    public ItemProcessor<BlogInfo, BlogInfo> blogInfoProcessor() {
        BlogInfoItemProcessor blogInfoItemProcessor = new BlogInfoItemProcessor();
        // 设置校验器
        blogInfoItemProcessor.setValidator(blogInfoValidator());
        return blogInfoItemProcessor;
    }

    /**
     * 注册processor校验器：在processor执行前进行校验
     *
     * @return processorValidator
     */
    @Bean
    public BlogInfoValidator<BlogInfo> blogInfoValidator() {
        return new BlogInfoValidator<>();
    }

    /**
     * 注册ItemWriter：processor处理完成的数据进行写入
     *
     * @return  ItemWriter
     */
    @Bean
    public ItemWriter<BlogInfo> blogInfoFileWriter() throws Exception {
        FlatFileItemWriter<BlogInfo> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("/Users/zhangyitian/Desktop/process-data.data"));
        itemWriter.setLineAggregator(new LineAggregator<BlogInfo>() {
            final ObjectMapper mapper = new ObjectMapper();

            @Override
            public String aggregate(BlogInfo blogInfo) {
                try {
                    return mapper.writeValueAsString(blogInfo);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to serialize, e", e);
                }
            }
        });
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    /**
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ItemWriter<BlogInfo> blogInfoDBWriter(DataSource dataSource) {
        // 使用jdbcBatchItemWriter写数据到db中
        JdbcBatchItemWriter<BlogInfo> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        String insertSql = "insert into BATCH_BIZ_TB_BLOG_INFO (blog_id, blog_title, blog_url, blog_author, blog_item, create_time, status) " +
                "values (:blogId, :blogTitle, :blogUrl, :blogAuthor, :blogItem, :createTime, :status)";
        writer.setSql(insertSql);
        writer.setDataSource(dataSource);
        return writer;
    }


}
