package com.chilly.demo;

import com.chilly.demo.common.Result;
import com.chilly.demo.model.Book;
import com.chilly.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class RestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;

    @BeforeEach
    void setUp() {
        String token = jwtUtil.generateToken("testUser", 3600);
        validToken = "Bearer " + token;
    }

    @Test
    public void testGetAll() {
        String url = "/api/books";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", validToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Result.class
        );
        log.info("结果: {}", response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(200);
    }

    @Test
    public void testAddBook() {
        // 1. 新增图书
        String url = "/api/books";
        Book book = new Book(null, "深入理解 JVM", "周志明", 129.0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", validToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> entity = new HttpEntity<>(book, headers);

        ResponseEntity<Result> addResponse = restTemplate.postForEntity(url, entity, Result.class);
        log.info("新增结果: {}", addResponse.getBody());
        assertThat(addResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(addResponse.getBody().getCode()).isEqualTo(200);

        // 2. 提取新生成的 ID
        Result result = addResponse.getBody();
        Integer newId = null;
        if (result != null && result.getData() != null) {
            // 由于 data 是 LinkedHashMap，直接强转取 id
            Map<String, Object> dataMap = (Map<String, Object>) result.getData();
            newId = (Integer) dataMap.get("id");
            log.info("新增图书 ID: {}", newId);
        }

        // 3. 如果成功获取 ID，执行删除
        if (newId != null) {
            String deleteUrl = "/api/books/" + newId;
            HttpHeaders deleteHeaders = new HttpHeaders();
            deleteHeaders.set("Authorization", validToken);
            HttpEntity<Void> deleteEntity = new HttpEntity<>(deleteHeaders);

            ResponseEntity<Result> deleteResponse = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    deleteEntity,
                    Result.class
            );
            log.info("删除响应: {}", deleteResponse.getBody());
            assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
        } else {
            log.warn("无法获取新增 ID，跳过删除步骤");
        }
    }
}