package com.yitian.examples.springbatchdemo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangyitian
 */
@SpringBootApplication(scanBasePackages = {"com.yitian.examples.springbatchdemo.*"})
@MapperScan(basePackages = "com.yitian.examples.springbatchdemo.mapper.*", annotationClass = Mapper.class)
public class SpringBatchDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDemoApplication.class, args);
    }

}
