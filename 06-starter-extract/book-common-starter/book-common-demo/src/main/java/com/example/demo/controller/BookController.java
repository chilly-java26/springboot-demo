package com.example.demo.controller;

import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @GetMapping
    public List<Book> list() {
        System.out.println("📖 查询所有图书...");
        return bookMapper.selectAll();
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Integer id) {
        System.out.println("📖 查询图书 ID=" + id);
        return bookMapper.selectById(id);
    }

    @PostMapping
    public String add(@RequestBody Book book) {
        System.out.println("📝 新增图书: " + book.getName());
        bookMapper.insert(book);
        return "添加成功，ID=" + book.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        System.out.println("🗑️ 删除图书 ID=" + id);
        bookMapper.deleteById(id);
        return "删除成功";
    }
}