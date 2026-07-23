package com.chilly.demo.interceptor;

import com.chilly.demo.util.JwtUtil;
import io.jsonwebtoken.JwtException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 1. 放行 Swagger 静态资源
        if (uri.startsWith("/swagger-ui/") || uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-resources/") || uri.startsWith("/webjars/")) {
            return true;
        }

        // 2. 获取 Authorization Header（注意：标准是 "Bearer <token>"，但为了简单我们只取纯 token）
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            log.warn("请求头缺少 Authorization");
            sendUnauthorized(response, "缺少 Authorization 头");
            return false;
        }

        // 3. 强制校验标准 Bearer 格式
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Authorization 头格式错误，期望 'Bearer <token>'，实际: {}", authHeader);
            sendUnauthorized(response, "Authorization 头格式错误，请使用 'Bearer <token>'");
            return false;
        }

        // 4. 提取 token（去掉 "Bearer " 前缀）
        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            log.warn("Bearer 后缺少 token 内容");
            sendUnauthorized(response, "Bearer 后缺少 token");
            return false;
        }

        // 5. 使用 JwtUtil 验证 token
        try {
            jwtUtil.validateToken(token);
            log.info("JWT 验证通过，token: {}", token);
            return true;
        } catch (JwtException e) {
            log.warn("JWT 验证失败: {}", e.getMessage());
            sendUnauthorized(response, "Token 无效或已过期");
            return false;
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"code\":401,\"msg\":\"" + msg + "\"}");
        out.flush();
    }
}