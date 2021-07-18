package com.yitian.examples.springbatchdemo.demo1;

import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

/**
 * Created by yitian.zyt on 2021/7/11
 */
@Slf4j
public class BlogInfoFileWriterListener implements ItemWriteListener<BlogInfo> {
    @Override
    public void beforeWrite(List<? extends BlogInfo> list) {
        for (BlogInfo item : list) {
            log.info(String.format("BeforeWrite, item: %s%n", item));
        }
        log.info("Chunk finished...");
    }

    @Override
    public void afterWrite(List<? extends BlogInfo> list) {

    }

    @Override
    public void onWriteError(Exception e, List<? extends BlogInfo> list) {
        try {
            log.info(String.format("%s%n", e.getMessage()));
            for (BlogInfo item : list) {
                log.info(String.format("Failed writing BlogInfo : %s", e.getMessage()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
