package com.yitian.examples.springbatchdemo.mapper;

import com.yitian.examples.springbatchdemo.domain.BlogInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yitian.zyt on 2021/7/18
 */
@Mapper
public interface BlogInfoMapper {

    @Insert("insert into BATCH_BIZ_TB_BLOG_INFO (blog_id, blog_title, blog_url, blog_author, blog_item, create_time, status) values (#{blogId}, #{blogTitle}, #{blogUrl}, #{blogAuthor}, #{blogItem}, #{createTime}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(BlogInfo blogInfo);


    @Select("select * from BATCH_BIZ_TB_BLOG_INFO where blog_item = #{blogItem}")
    List<BlogInfo> queryInfoByItem(String blogItem);
}
