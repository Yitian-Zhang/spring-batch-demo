package com.yitian.examples.springbatchdemo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yitian.zyt on 2021/7/11
 */
@Data
public class BlogInfo implements Serializable {
    private Long blogId;
    private String blogTitle;
    private String blogUrl;
    private String blogAuthor;
    private String blogItem;
    private Date createTime;
    private Integer status;


    @Override
    public String toString() {
        return "BlogInfo{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogUrl='" + blogUrl + '\'' +
                ", blogAuthor='" + blogAuthor + '\'' +
                ", blogItem='" + blogItem + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
