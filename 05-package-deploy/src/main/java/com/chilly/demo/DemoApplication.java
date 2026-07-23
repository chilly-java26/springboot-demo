package com.chilly.demo;

import com.chilly.demo.servlet.CustomServlet;
import com.chilly.demo.model.Book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        Book book = new Book(1, "测试", "作者", 99.0);
        System.out.println("book id: "+book.getId());  // 如果报错或输出 null，说明 getter 没生成

        SpringApplication.run(DemoApplication.class, args);
    }

    // 在配置类中增加
    @Bean
    public ServletRegistrationBean<CustomServlet> customServletBean() {
        ServletRegistrationBean<CustomServlet> bean = new ServletRegistrationBean<>(
                new CustomServlet(),
                "/custom-servlet"   // 访问路径
        );
        bean.setLoadOnStartup(1);
        return bean;
    }
}