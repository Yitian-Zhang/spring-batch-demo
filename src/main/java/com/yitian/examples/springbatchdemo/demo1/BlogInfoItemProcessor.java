package com.yitian.examples.springbatchdemo.demo1;

import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * Created by yitian.zyt on 2021/7/11
 */
public class BlogInfoItemProcessor extends ValidatingItemProcessor<BlogInfo> {

    @Override
    public BlogInfo process(BlogInfo item) throws ValidationException {
        // 需要执行super.process(item)才会调用自定义校验器
        super.process(item);

        // 对数据进行简单的处理
        if (item.getBlogItem().equals("springboot")) {
            item.setBlogTitle("Processed SpringBoot BlogItem");
        } else {
            item.setBlogTitle("Processed Other BlogItem");
        }
        return item;
    }
}
