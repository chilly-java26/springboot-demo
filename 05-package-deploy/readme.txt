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
第一步：修改pom打War包，maven war plugin
添加 Tomcat 依赖
添加ServletInitializer.java

打包：
mvn clean package

部署：
cp target/springboot-demo-1.0.war ~/Tomcat/apache-tomcat-9.0.120/webapps 
~/Tomcat/apache-tomcat-9.0.120/bin/startup.sh

访问：
http://localhost:8080/springboot-demo-1.0/index
localStorage.setItem('token', 'Bearer <你的JWT>')

多实例部署
修改application.yml，不写死端口
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
mvn clean package
java -jar target/springboot-demo.war --server.port=8081 &
java -jar target/springboot-demo.war --server.port=8082 &
java -jar target/springboot-demo.war --server.port=8083 &

vi /opt/homebrew/etc/nginx/nginx.conf #编写 nginx.conf 配置负载均衡
nginx -s reload
测试（多次刷新，负载均衡）：http://localhost

运行：
run.sh

查看首页：
http://localhost:8080/index

查看Servlet返回：
http://localhost:8080/custom-servlet

文档：
http://localhost:8080/swagger-ui/index.html

自动配置原理（约定优于配置）：
一句话：
Spring Boot 启动时，自动扫描 classpath 中的依赖，
根据条件决定是否创建对应的 Bean，
让你不用写任何配置就能使用各种功能。

1）SpringBoot 的自动配置原理是基于 
@EnableAutoConfiguration 和 SpringFactoriesLoader 实现的。

2）启动时，SpringBoot 会读取 Classpath 下所有 META-INF/spring.factories 文件中
配置的 EnableAutoConfiguration 键值，加载成百上千个 xxxAutoConfiguration 配置类。

这些配置类用 @Conditional 系列注解（如 @ConditionalOnClass、@ConditionalOnMissingBean）
来判断当前环境是否满足条件，只有满足条件时才会创建对应的 Bean 注入容器。

3）这样开发者只需要“引入 Starter 依赖”，框架就能“自动装配好必要的组件”，无需手动配置 XML。

springboot-demo-1.0.war
└── WEB-INF/
    └── lib/
        └── spring-boot-autoconfigure-2.7.0.jar
            └── META-INF/
                └── spring.factories
                
┌─────────────────────────────────────────────────────┐
│                   启动 Spring Boot                    │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│      @EnableAutoConfiguration 开启自动配置           │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│      加载 META-INF/spring.factories                 │
│      获取所有 EnableAutoConfiguration 列表           │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│      遍历每个自动配置类                              │
│      检查 @Conditional 条件                         │
│                                                    │
│      有 Redis 依赖？ → 是 → RedisAutoConfiguration  │
│      有 DataSource？  → 是 → DataSourceAutoConfig   │
│      有 Couchbase？   → 否 → 跳过 ✅               │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│      条件满足 → 创建 Bean                           │
│      条件不满足 → 跳过                              │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│              IOC 容器准备好了 ✅                     │
└─────────────────────────────────────────────────────┘

Maven scopes:
compile（默认）："所有阶段都需要"，并且会打进最终包。
provided：编译和测试时需要，但"运行时由容器提供"，不会打进包。
runtime：编译时不需要，但"测试和运行时需要"，会打进包（JDBC 驱动）。
test：只在"编译和运行测试时需要"，不会打进最终包。
system：需要"本地 JAR"，已废弃，不推荐使用。
import：只用于 <dependencyManagement> 导入 BOM 版本管理，"不引入实际依赖"。