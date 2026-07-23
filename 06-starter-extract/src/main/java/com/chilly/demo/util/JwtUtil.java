package com.chilly.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 JWT Token（不带 "Bearer " 前缀）
     * @param subject 用户名或其他标识
     * @param expireSeconds 过期秒数（如 3600 为 1 小时）
     * @return JWT 字符串
     */
    public String generateToken(String subject, long expireSeconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireSeconds * 1000);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成 JWT Token，默认过期时间 1 小时
     */
    public String generateToken(String subject) {
        return generateToken(subject, 3600);
    }

    /**
     * 验证 JWT 并返回 Claims（包含用户信息等）
     * @param token JWT 字符串
     * @return Claims 对象（可从中获取 subject、自定义字段等）
     * @throws io.jsonwebtoken.JwtException 如果签名无效、过期或格式错误
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 检查 token 是否过期（可选，直接放在 validateToken 里一起验证）
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = validateToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}