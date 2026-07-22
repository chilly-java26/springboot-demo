package com.chilly.demo.service;

import com.chilly.demo.config.BookConfig;
import com.chilly.demo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {

    @Autowired
    private BookConfig bookConfig;  // 沿用项目一的配置注入

    // 内存数据库（线程安全）
    private final Map<Integer, Book> bookStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    // 项目一原有的初始化数据逻辑，现在放到这里
    @PostConstruct
    public void init() {
        // 从 BookConfig 读取默认书籍（如果项目一里配了的话），否则给个默认假数据
        List<Book> defaultBooks = bookConfig.getDefaultBooks(); 
        if (defaultBooks != null && !defaultBooks.isEmpty()) {
            defaultBooks.forEach(book -> {
                book.setId(idGenerator.getAndIncrement());
                bookStore.put(book.getId(), book);
            });
        } else {
            // 兜底假数据（保证启动有内容）
            Book b1 = new Book(1, "Spring Boot 实战", "Craig Walls", 69.0);
            Book b2 = new Book(2, "Java 并发编程实战", "Brian Goetz", 89.0);
            bookStore.put(1, b1);
            bookStore.put(2, b2);
            idGenerator.set(3);
        }
    }

    // 查询全部
    public List<Book> findAll() {
        return new ArrayList<>(bookStore.values());
    }

    // 按 ID 查询
    public Book findById(Integer id) {
        return bookStore.get(id);
    }

    // 新增（ID 由内部生成）
    public Book add(Book book) {
        Integer newId = idGenerator.getAndIncrement();
        book.setId(newId);
        bookStore.put(newId, book);
        return book;
    }

    // 修改
    public Book update(Book book) {
        if (!bookStore.containsKey(book.getId())) {
            return null;
        }
        bookStore.put(book.getId(), book);
        return book;
    }

    // 删除
    public boolean delete(Integer id) {
        return bookStore.remove(id) != null;
    }
}