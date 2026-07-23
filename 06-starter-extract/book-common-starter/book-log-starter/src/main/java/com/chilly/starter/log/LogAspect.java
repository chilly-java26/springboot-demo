package com.chilly.starter.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private final LogProperties properties;

    public LogAspect(LogProperties properties) {
        this.properties = properties;
    }

    @Around("execution(public * *..controller..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!properties.isEnabled()) {
            return joinPoint.proceed();
        }

        long start = System.currentTimeMillis();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();

        log.info("===== 请求开始 =====");
        log.info("URL: {}", request.getRequestURL());
        log.info("Method: {}", request.getMethod());
        log.info("IP: {}", request.getRemoteAddr());
        log.info("Class: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Params: {}", Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        long cost = System.currentTimeMillis() - start;
        log.info("Return: {}", result);
        log.info("Cost: {} ms", cost);
        log.info("===== 请求结束 =====");

        return result;
    }
}