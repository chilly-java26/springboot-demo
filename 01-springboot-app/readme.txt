项目一：图书管理·单体基础版

涉及知识点
概述与快速开始、pom.xml 父项目分析、dependencies 依赖分析、场景启动器、Controller 分析、@RestController 注解、如何统一返回、概述总结

项目结构
SpringBoot 项目结构
01-springboot-app/
├── pom.xml
├── run.sh
├── .gitignore
└── src
    └── main
        ├── java
        │   └── com
        │       └── chilly
        │           └── demo
        │               ├── DemoApplication.java
        │               ├── controller
        │               │   └── BookController.java
        │               ├── config
        │               │   └── BookConfig.java
        │               ├── model
        │               │   └── Book.java
        │               └── service
        │                   └── BookService.java
        └── resources
            ├── application.yml
            ├── application-dev.yml
            ├── application-test.yml
            └── logback-spring.xml

配置文件
两种配置文件介绍、yml 语法及加载顺序、外部约定配置文件加载顺序、Profile 文件加载、所有配置文件顺序加载、YML 值注入到 Bean、Lombok 使用、自动提示、松散绑定、@Value 获取值、占位符、热部署

单元测试
SpringBoot 下单元测试、Java 类方式注入 Bean 到 IOC

日志体系
混乱的日志体系、log4j、jul、JCL、slf4j+logback、log4j2、slf4j 整合各框架、日志适配器统一输出、SpringBoot 日志框架、自定义日志配置、切换日志框架、@Slf4j 注解

功能描述
	•	用 Controller 返回"图书列表"静态 JSON
	•	yml 配置项目信息并注入 Bean
	•	单元测试验证 Controller
	•	配置 logback 输出控制台和文件日志
	•	Profile 区分开发/测试环境

实现步骤：
// 配置依赖，启动类，图书类
第 1 步：pom.xml
第 2 步：启动类 ...demo/DemoApplication.java
第 3 步：图书实体类 ...demo/model/Book.java

// BookConfig负责把 application.yml 里的配置映射成 Java 对象，让Service读默认数据
第 4 步：配置文件（演示 yml 值注入）...demo/config/BookConfig.java
第 5 步：application.yml（主配置 + Profile 切换）src/main/resources/application.yml
第 6 步：开发环境配置 src/main/resources/application-dev.yml
第 7 步：测试环境配置 src/main/resources/application-test.yml

// Service负责准备数据，未来可改成接入数据库，让Controller拿数据
第 8 步：BookService（演示 @Value 和注入）...demo/service/BookService.java

// Controller负责提供HTTP服务，调用Service拿数据，把结果封装成统一格式返回给前端
第 9 步：BookController（统一返回 + 测试用）...demo/controller/BookController.java

启动：
./run.sh

测试：
http://localhost:8080/books
http://localhost:8080/books/3