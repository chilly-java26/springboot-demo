package com.chilly.demo.mapper;

import com.chilly.demo.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper  // 让 Spring 扫描到这个接口
public interface BookMapper {

    // 查询全部
    List<Book> selectAll();

    // 按 ID 查询
    Book selectById(@Param("id") Integer id);

    // 新增（返回自增主键）
    int insert(Book book);

    // 修改
    int update(Book book);

    // 删除
    int deleteById(@Param("id") Integer id);
}