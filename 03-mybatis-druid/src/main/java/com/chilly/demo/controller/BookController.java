package com.chilly.demo.controller;

import com.chilly.demo.common.Result;
import com.chilly.demo.model.Book;
import com.chilly.demo.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@Api(tags = "book management api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @ApiOperation("get all books")
    public Result<List<Book>> list() {
        log.info("收到查询所有图书请求");
        return Result.success(bookService.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation("get book by id")
    public Result<Book> getById(@PathVariable @ApiParam("图书ID") Integer id) {
        Book book = bookService.findById(id);
        return book == null ? Result.error("图书不存在") : Result.success(book);
    }

    @PostMapping
    @ApiOperation("create a new book")
    public Result<Book> add(@RequestBody @ApiParam("图书JSON") Book book) {
        Book newBook = bookService.add(book);
        return Result.success(newBook);
    }

    @PutMapping("/{id}")
    @ApiOperation("update a book by id")
    public Result<Book> update(@PathVariable Integer id, @RequestBody Book book) {
        book.setId(id);
        Book updated = bookService.update(book);
        return updated == null ? Result.error("图书不存在") : Result.success(updated);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("delete a book by id")
    public Result<String> delete(@PathVariable Integer id) {
        boolean success = bookService.delete(id);
        return success ? Result.success("删除成功") : Result.error("图书不存在");
    }
}