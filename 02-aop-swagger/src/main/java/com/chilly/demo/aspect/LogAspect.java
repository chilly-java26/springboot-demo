package com.chilly.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.chilly.demo.controller..*.*(..))")
    public void controllerLog() {}

    @Around("controllerLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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