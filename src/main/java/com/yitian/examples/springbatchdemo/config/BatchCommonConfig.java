package com.yitian.examples.springbatchdemo.config;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by yitian.zyt on 2021/7/17
 */
@Configuration
public class BatchCommonConfig {

    /**
     * JobRepository定义：Job的注册容器以及和数据库打交道（事务管理等）
     * SpringBatch启动也需要sql链接
     *
     * @param dataSource            Spring自动加载的DS
     * @param transactionManager    默认事务管理器
     * @return                      JobRepository
     * @throws Exception            异常
     */
    @Bean
    public JobRepository myJobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDatabaseType("mysql");
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDataSource(dataSource);
        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * jobLauncher定义： job的启动器,绑定相关的jobRepository
     *
     * @param dataSource            Spring自动加载的DS
     * @param transactionManager    默认事务管理器
     * @return                      JobRepository
     * @throws Exception            异常
     */
    @Bean
    public SimpleJobLauncher myJobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(myJobRepository(dataSource, transactionManager));
        return jobLauncher;
    }
}
