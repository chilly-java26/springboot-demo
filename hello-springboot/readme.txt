项目结构是什么？
hello-springboot/
├── pom.xml <- 1
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── demo/
        │               ├── DemoApplication.java <- 2
        │               └── controller/
        │                   └── HelloController.java <- 3
        └── resources/
            └── application.yml <- 4

如何运行？
mvn spring-boot:run

如何测试？
http://localhost:8080/hello

1. 编写pom.xml
- 定义项目GAV
- 定义Java版本
- 定义父POM：SB starter parent
- 引入依赖：SB starter web

1.1 spring-boot-starter-parent是什么？
Maven父POM文件，Spring Boot 官方为你提前写好的最佳配置。

1.2 spring-boot-starter-parent为啥写在parent里？
在 Spring Boot 项目中，将 spring-boot-starter-parent 写在 <parent> 标签里，
可以带来以下4个好处：
(1) 极简的版本管理：pom.xml 中引入大部分 Spring Boot 官方组件时，不需要再写 <version> 标签。
(2) 配置好了默认的maven插件：
- spring-boot-maven-plugin：可以用mvn spring-boot:run执行程序
- maven-compiler-plugin：默认编译级别
- maven-surefire-plugin：默认的单元测试跳过策略等
(3) 统一的资源过滤和编码：
设置了合理的默认属性（Properties）：
- project.build.sourceEncoding：默认设为 UTF-8。
- java.version：默认为 1.8（2.7.3 版对应 Java 8）。
(4) 解决“依赖地狱”（Dependency Management）
- 管理了常用的第三方库（如 Jackson、Log4j2、Elasticsearch 等）的最佳兼容版本
- 只需声明 groupId 和 artifactId，版本号自动搞定

1.3 spring-boot-starter-web是什么？
spring-boot-starter-web只要引入这一个依赖，你就拥有了一个完整的 Web 开发环境。

2. 编写启动类：DemoApplicaiton.java
用@SpringBootApplication来装饰，实现三件核心大事：
- 开启自动配置
- 开启组件扫描
- 标记配置入口

2.1 @EnableAutoConfiguration（开启自动配置机制）
效果：
根据引入的 Jar 包来决定加载哪些 Bean，让开发者完全脱离了 XML 配置。
原理：
Spring Boot 在启动时，会去 classpath 下查找所有 META-INF 中定义的配置类。

2.2 @ComponentScan（开启注解扫描）
效果：自动实例化为 Bean 放入 IoC 容器。
查找带有 @Component、@Service、@Repository、@Controller 等注解的类（启动类所在包及子包）

2.3 @SpringBootConfiguration（配置入口）
它的底层就是 @Configuration，表明这个类是一个 Spring 配置类。

3. 编写Controller类：HelloController.java
@RestController是什么？
@RestController 是 Spring 4.0 引入的专用注解，本质上是一个“复合注解”。
拆解注解：@Controller + @ResponseBody。
做了什么：告诉 Spring 这个类负责接收 HTTP 请求；标记所有方法返回数据（JSON/XML/Text）而非视图页面。
具体解释：当浏览器访问 /hello 时，方法执行，"Hello SpringBoot" 直接写入 HTTP 响应体返回给客户端（而不是寻找 hello.jsp 文件）。

与旧版对比（@Controller）：如果你只使用 @Controller，必须额外在方法上加 @ResponseBody。@RestController 省掉了在每个方法上重复写 @ResponseBody 的麻烦。

4. 编写业务配置：application.yaml
- 配置数据库连接地址
- 配置 Redis 缓存过期时间
- 配置业务开关（如 feature.enabled: true）
