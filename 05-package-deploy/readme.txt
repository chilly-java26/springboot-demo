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

======

项目目录：
项目五：图书管理·原理与发布版
│
├── 📄 pom.xml                                                       【已修改】
│   ├── 新增：<packaging>war</packaging>（切换打包方式）
│   ├── 新增：spring-boot-starter-tomcat 作用域设为 provided
│   └── 新增：maven-war-plugin（可选，用于配置）
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 chilly/
│   │   │           └── 📁 demo/
│   │   │               │
│   │   │               ├── 📄 DemoApplication.java                  【不变】
│   │   │               │
│   │   │               ├── 📄 ServletInitializer.java              【✨ 新增】
│   │   │               │   └── 继承 SpringBootServletInitializer，配置外部 Tomcat 入口
│   │   │               │
│   │   │               ├── 📁 common/     （Result.java）           【不变】
│   │   │               ├── 📁 config/     （Swagger/WebMvc）        【不变】
│   │   │               ├── 📁 controller/ （BookController/PageController）【不变】
│   │   │               ├── 📁 service/    （BookService/...）       【不变】
│   │   │               ├── 📁 mapper/     （BookMapper）            【不变】
│   │   │               ├── 📁 interceptor/（TokenInterceptor）      【不变】
│   │   │               ├── 📁 aspect/     （LogAspect）             【不变】
│   │   │               ├── 📁 util/       （JwtUtil）               【不变】
│   │   │               └── 📁 servlet/    （CustomServlet）         【不变】
│   │   │
│   │   ├── 📁 resources/
│   │   │   ├── 📄 application.yml                                   【已修改】
│   │   │   │   └── 新增：server.port=8080（多实例时用命令行覆盖）
│   │   │   ├── 📄 application-dev.yml / test.yml                   【不变】
│   │   │   ├── 📄 logback-spring.xml                               【不变】
│   │   │   ├── 📁 mapper/    （BookMapper.xml）                    【不变】
│   │   │   └── 📁 templates/ （bookList.ftl）                      【不变】
│   │   │
│   │   └── 📁 webapp/                                               【✨ 新增】
│   │       └── 📄 WEB-INF/                                         （War 包标准结构，可空）
│   │
│   └── 📁 test/                                                    【不变】
│
├── 📄 nginx.conf                                                    【✨ 新增】（配置文件）
│   └── 配置 upstream 负载均衡到三个实例
│
└── 📄 start-cluster.sh                                              【✨ 新增】（启动脚本）
    └── 批量启动 8081/8082/8083 三个实例

修改步骤：


运行：
run.sh

查看首页：
http://localhost:8080/index

查看Servlet返回：
http://localhost:8080/custom-servlet

文档：
http://localhost:8080/swagger-ui/index.html

知识点整理：
