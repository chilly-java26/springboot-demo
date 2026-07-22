package com.chilly.demo.model;

// 导入代码生成工具库lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
// @Data 等于同时加了：
@Getter        // 所有字段的 get 方法
@Setter        // 所有字段的 set 方法
@ToString      // toString() 方法
@EqualsAndHashCode  // equals() 和 hashCode() 方法
@RequiredArgsConstructor  // 必要参数的构造方法（被final和@NonNull修饰）
*/
@Data
@NoArgsConstructor  // 无参构造函数
@AllArgsConstructor // 全参构造函数
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Double price;
}