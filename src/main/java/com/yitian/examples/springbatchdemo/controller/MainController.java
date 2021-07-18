package com.yitian.examples.springbatchdemo.controller;

import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import com.yitian.examples.springbatchdemo.mapper.BlogInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yitian.zyt on 2021/7/18
 */
@RequestMapping("/main")
public class MainController {

    @Autowired
    private BlogInfoMapper blogInfoMapper;

    @GetMapping("/insert/blogInfo")
    public String insertBlogInfo() {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setBlogId(100L);
        blogInfo.setBlogTitle("测试Blog");
        int res = blogInfoMapper.insert(blogInfo);
        return "SUCCESS";
    }
}
