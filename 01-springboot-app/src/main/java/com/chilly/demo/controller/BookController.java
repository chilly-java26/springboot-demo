package com.chilly.demo.controller;

import com.chilly.demo.model.Book;
import com.chilly.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Map<String, Object> list() {
        log.info("收到查询所有图书请求");
        List<Book> books = bookService.getAllBooks();

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", books);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        log.info("收到查询单本图书请求 id: {}", id);
        Book book = bookService.getBookById(id);

        Map<String, Object> result = new HashMap<>();
        if (book != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", book);
        } else {
            result.put("code", 404);
            result.put("message", "图书不存在");
            result.put("data", null);
        }
        return result;
    }
}