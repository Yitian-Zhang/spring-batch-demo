package com.yitian.examples.springbatchdemo.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by yitian.zyt on 2021/7/11
 */
@Slf4j
public class BlogInfoJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("[BlogInfoJobListener beforeJob] jobId={}", jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("[BlogInfoJobListener afterJob] jobId={}", jobExecution.getJobId());
    }
}
