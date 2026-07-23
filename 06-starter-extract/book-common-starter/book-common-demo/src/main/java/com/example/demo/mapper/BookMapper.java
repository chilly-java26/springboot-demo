package com.example.demo.mapper;

import com.example.demo.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    List<Book> selectAll();
    Book selectById(@Param("id") Integer id);
    int insert(Book book);
    int update(Book book);
    int deleteById(@Param("id") Integer id);
}