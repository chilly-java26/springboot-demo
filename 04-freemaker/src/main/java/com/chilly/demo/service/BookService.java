package com.chilly.demo.service;

import com.chilly.demo.mapper.BookMapper;
import com.chilly.demo.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    // 1. 查询全部
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookMapper.selectAll();
    }

    // 2. 按 ID 查询
    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        return bookMapper.selectById(id);
    }

    // 3. 新增（事务管理：失败则回滚）
    @Transactional
    public Book add(Book book) {
        // 忽略客户传的id
        book.setId(null);
        bookMapper.insert(book);
        // MyBatis 会把自增的 ID 回填
        log.info("新增图书成功，生成 ID: {}", book.getId());
        return book;
    }

    // 4. 修改
    @Transactional
    public Book update(Book book) {
        // 先检查是否存在（防止更新不存在的记录）
        Book existing = bookMapper.selectById(book.getId());
        if (existing == null) {
            return null;
        }
        int rows = bookMapper.update(book);
        return rows > 0 ? book : null;
    }

    // 5. 删除
    @Transactional
    public boolean delete(Integer id) {
        return bookMapper.deleteById(id) > 0;
    }

    // 6. 分页查询
    @Transactional(readOnly = true)
    public PageInfo<Book> getBooksByPage(int pageNum, int pageSize) {
        // PageHelper 必须紧跟在要分页的查询之前
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll();
        return new PageInfo<>(list);
    }
}