package com.chilly.demo;

import com.chilly.demo.model.Book;
import com.chilly.demo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;  // 如果没有这个 Bean，可在启动类加 @Bean 定义

    @Test
    public void testGetAll() {
        String url = "http://localhost:8080/api/books";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "admin-token");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Result.class
        );
        log.info("结果: {}", response.getBody());
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    public void testAddBook() {
        String url = "http://localhost:8080/api/books";
        Book book = new Book(null, "深入理解 JVM", "周志明", 129.0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "admin-token");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> entity = new HttpEntity<>(book, headers);

        ResponseEntity<Result> response = restTemplate.postForEntity(url, entity, Result.class);
        log.info("新增结果: {}", response.getBody());
    }
}