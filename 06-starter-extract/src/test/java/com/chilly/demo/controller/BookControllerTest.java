package com.chilly.demo.controller;

import com.chilly.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;

    @BeforeEach
    void setUp() {
        // 生成有效 Token（1 小时过期）
        String token = jwtUtil.generateToken("testUser", 3600);
        validToken = "Bearer " + token;
    }

    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/api/books")  // 修正路径，加上 /api
                .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/books/1")  // 修正路径
                .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("Spring Boot 实战"));  // 确保数据库有这条数据
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/books/999")  // 修正路径
                .header("Authorization", validToken))
                .andExpect(status().isOk())  // HTTP 状态码仍为 200
                .andExpect(jsonPath("$.code").value(500))  // 或 404，取决于你的错误返回码
                .andExpect(jsonPath("$.msg").value("图书不存在"));
    }
}