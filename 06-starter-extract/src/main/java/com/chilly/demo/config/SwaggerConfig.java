package com.chilly.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chilly.demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("图书管理 API 文档")
                .description("项目二：API 增强版")
                .version("1.0")
                .contact(new Contact("Chilly", "http://localhost:8080", "chilly@demo.com"))
                .build();
    }

    // ===支持Authorization header===

    private List<SecurityScheme> securitySchemes() {
        // UI tag name, header name, position
        return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<SecurityContext> securityContexts() {
        SecurityContext context = SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)  // 对所有接口生效
                .build();
        return Collections.singletonList(context);
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("book:all", "Full access to book APIs");
        // UI tag name
        return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
    }
}