项目一回顾：
包根路径：com.chilly.demo
已有类：DemoApplication（启动类）、Book（实体）、BookConfig（配置映射）、BookService（服务层，目前提供静态数据）、BookController（控制器）
配置文件：application.yml（主配置）、application-dev.yml、application-test.yml、logback-spring.xml
已有接口：GET /books 和 GET /books/{id}（返回静态 JSON）

项目二目标：在不破坏原有结构的前提下，增加以下能力——
将接口升级为完整 RESTful（POST/PUT/DELETE）
接入 Swagger 生成文档
用 AOP 记录请求日志
用拦截器校验 Header 中的 Token

项目三目标：在不破坏原有Controller接口的前提下，
将数据源从内存Map替换为MySQL数据库，
集成MyBatis作为ORM框架，
使用Druid连接池管理数据库连接，
支持事务管理，并提供分页查询能力。

项目四：图书管理·前端交互版
迭代内容
	•	FreeMarker 写图书列表页 + 新增表单
	•	Ajax 调用后端接口
	•	CORS 跨域解决
	•	Json 格式优化
	•	切换嵌入式容器对比
	•	注册自定义 Servlet

项目五：图书管理·原理与发布版
迭代内容
	•	打 War 包部署到外部 Tomcat
	•	启动多实例 + Nginx 负载均衡
	•	Debug 跟踪自动配置过程
	•	【选】 深入 @EnableAutoConfiguration 原理
	•	【选】 输出面试回答话术
	•	【选】 注意 2.7.3 版本差异

项目六：图书管理·自动装配版【后】
迭代内容
	•	【选】 把项目五中的日志 AOP 模块抽取为自定义 starter
	•	【选】 把 Druid + MyBatis 整合配置抽取为自定义 starter
	•	【选】 写单元测试验证两个自定义 starter
	•	【选】 在其他项目中引入测试
	•	【选】 总结 starter 开发规范和流程

======

项目目录：
book-parent/                         (新建父工程，或直接在项目五同级目录)
├── book-common-starter/              (父模块，pom 聚合)
│   ├── pom.xml
│   ├── book-log-starter/             (子模块1：日志 AOP Starter)
│   │   ├── pom.xml
│   │   └── src/main/java/com/chilly/starter/log/
│   │       ├── LogAutoConfiguration.java
│   │       ├── LogProperties.java
│   │       ├── LogAspect.java
│   │       └── EnableLogging.java (可选)
│   │   └── src/main/resources/META-INF/
│   │       └── spring.factories
│   │       └── spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports (2.7+)
│   ├── book-mybatis-starter/         (子模块2：MyBatis+Druid Starter)
│   │   ├── pom.xml
│   │   └── src/main/java/com/chilly/starter/mybatis/
│   │       ├── MyBatisAutoConfiguration.java
│   │       ├── MyBatisProperties.java
│   │       ├── DruidDataSourceConfig.java (可选，也可以合并)
│   │       └── EnableMyBatisPlus (可选)
│   │   └── src/main/resources/META-INF/
│   │       └── spring.factories (或 imports)
│   └── (可选) book-common-demo/       (测试项目，引入两个 starter，验证)
│       ├── pom.xml
│       └── src/main/java/... (一个简单的 Controller)
└── 原项目五         (不再改动，仅作参考)

修改步骤：
1. 创建父工程和子模块
2. 开发日志 Starter (book-log-starter)
3. 开发 MyBatis+Druid Starter (book-mybatis-starter)
4. 安装到本地 Maven 仓库
5. 创建测试项目验证
6. 总结 starter 开发规范

Starter项目结构：
my-spring-boot-starter/
├── pom.xml
└── src/main/java/com/example/
    ├── MyAutoConfiguration.java          ← 自动配置类
    ├── MyProperties.java                 ← 配置属性类
    └── MyService.java                    ← 业务类（可选）
└── src/main/resources/
    └── META-INF/
        └── spring.factories              ← 注册配置
1. Properties 类（配置属性）
作用：绑定 application.yml 中的配置，让用户能自定义 Starter 的行为。
2. AutoConfiguration 类（自动配置）
作用：根据 Properties 中的配置，创建 Bean 注入 IOC 容器。

测试项目结构：
book-starter-test/
├── pom.xml
└── src/main/
    ├── java/com/example/demo/
    │   ├── DemoApplication.java
    │   ├── controller/
    │   │   └── BookController.java
    │   ├── mapper/
    │   │   └── BookMapper.java
    │   └── model/
    │       └── Book.java
    └── resources/
        ├── application.yml
        └── mapper/
            └── BookMapper.xml
