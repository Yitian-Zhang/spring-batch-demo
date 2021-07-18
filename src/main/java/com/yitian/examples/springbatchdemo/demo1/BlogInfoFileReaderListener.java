package com.yitian.examples.springbatchdemo.demo1;

import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

/**
 * Created by yitian.zyt on 2021/7/11
 */
@Slf4j
public class BlogInfoFileReaderListener implements ItemReadListener<BlogInfo> {
    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(BlogInfo blogInfo) {

    }

    @Override
    public void onReadError(Exception e) {
        try {
            log.info(String.format("%s%n", e.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
