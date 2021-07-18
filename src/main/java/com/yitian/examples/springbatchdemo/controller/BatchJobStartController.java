package com.yitian.examples.springbatchdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 通过Web请求启动Job任务
 *
 * @author zhangyitian
 * Created by yitian.zyt on 2021/7/11
 */
@Slf4j
@RestController
@RequestMapping("/batch/process")
public class BatchJobStartController {

    @Resource
    private SimpleJobLauncher myJobLauncher;

    @Resource
    private Job blogInfoJob;

    @GetMapping("/blogInfoJob")
    public String startBlogInfoJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
            myJobLauncher.run(blogInfoJob, jobParameters);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("");
            return "FAILED";
        }
    }

    @GetMapping("/otherJob")
    public String startOtherJob() {
        return "SUCCESS";
    }
}
