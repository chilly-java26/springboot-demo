package com.chilly.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 访问根路径或 /index 跳转到图书列表页
    @GetMapping({"/", "/index"})
    public String index() {
        return "bookList";  // 对应 src/main/resources/templates/bookList.ftl
    }
}