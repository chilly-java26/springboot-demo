package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data          // 生成 getter/setter、toString、equals、hashCode
@NoArgsConstructor  // 无参构造
@AllArgsConstructor // 全参构造
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Double price;
}