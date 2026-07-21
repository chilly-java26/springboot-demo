package com.chilly.demo.service;

import com.chilly.demo.config.BookConfig;
import com.chilly.demo.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j // 生成一个 log 对象
@Service
public class BookService {

    @Value("${book.default-author}")
    private String defaultAuthor;

    private final BookConfig bookConfig;

    public BookService(BookConfig bookConfig) {
        this.bookConfig = bookConfig;
    }

    private final List<Book> bookList = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info("BookService 初始化完成");
        log.info("@Value 取到的 defaultAuthor: {}", defaultAuthor);
        log.info("@ConfigurationProperties 取到的 maxPrice: {}", bookConfig.getMaxPrice());

        bookList.add(new Book(1, "Java入门", "张三", 59.0));
        bookList.add(new Book(2, "SpringBoot实战", "李四", 79.0));
        bookList.add(new Book(3, "MySQL必知必会", defaultAuthor, 49.0));
    }

    public List<Book> getAllBooks() {
        log.debug("查询所有图书，当前数量: {}", bookList.size());
        return bookList;
    }

    public Book getBookById(Integer id) {
        log.info("查询图书 id: {}", id);
        return bookList.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
