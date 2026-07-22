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

======

项目目录：
01-springboot-app/
├── pom.xml（修改：新增 Swagger + AOP 依赖）
├── run.sh
└── src
    └── main
        ├── java
        │   └── com
        │       └── chilly
        │           └── demo
        │               ├── DemoApplication.java（不变）
        │               ├── common（新增包）
        │               │   └── Result.java          # 统一返回结果封装
        │               ├── controller
        │               │   └── BookController.java  （重构为 RESTful）
        │               ├── config（新增配置类）
        │               │   ├── BookConfig.java      （原配置类，不变）
        │               │   ├── SwaggerConfig.java   （新增）
        │               │   └── WebMvcConfig.java    （新增，注册拦截器）
        │               ├── interceptor（新增包）
        │               │   └── TokenInterceptor.java
        │               ├── aspect（新增包）
        │               │   └── LogAspect.java
        │               ├── model
        │               │   └── Book.java（加 Swagger 注解）
        │               └── service
        │                   └── BookService.java（扩展为内存 CRUD）
        └── resources
            ├── application.yml（追加 spring.mvc.pathmatch 配置）
            ├── application-dev.yml（不变）
            ├── application-test.yml（不变）
            └── logback-spring.xml（不变）

修改步骤：
1. 更新pom文件：新增swagger和aop依赖
2. 添加common/Result.java：封装统一返回结果（泛型）
3. 升级 BookService.java：从只读变为内存 CRUD 数据源
4. 重构 BookController.java：RESTful + Swagger 注解
5. 新增 SwaggerConfig.java：配置文档
6. 新增 LogAspect.java：AOP 记录请求日志
7. 新增 注册器 WebMvcConfig + 拦截器 TokenInterceptor
8. 修改 application.yml：适配 Swagger + 保持原有配置
9. RestTemplate 客户端测试：单元测试

运行：
run.sh

文档：
http://localhost:8080/swagger-ui/index.html

总结：
项目二：图书管理·API 增强版
│
├── 模块一：Rest 接口开发（405-412）
│   │
│   ├── 知识点 405：Rest 接口开发（一）
│   │   ├── RESTful 风格概念：用 HTTP 方法（GET/POST/PUT/DELETE）表示操作
│   │   ├── URL 设计：名词复数，如 /api/books
│   │   ├── @RestController：类上标记，返回 JSON
│   │   ├── @RequestMapping("/api/books")：定义根路径
│   │   ├── @GetMapping：查询全部列表
│   │   ├── @PostMapping：新增数据
│   │   ├── @PutMapping：修改数据
│   │   ├── @DeleteMapping：删除数据
│   │   └── @PathVariable：从 URL 路径中取值，如 /books/{id}
│   │
│   ├── 知识点 406：@RequestBody 详解
│   │   ├── 作用：将 HTTP 请求体中的 JSON 自动绑定为 Java 对象
│   │   ├── 前提：请求头 Content-Type: application/json
│   │   ├── 配合 @Valid 可做参数校验
│   │   └── 用于 POST / PUT 方法接收前端数据
│   │
│   ├── 知识点 407：创建 RestTemplate（Java类方式）
│   │   ├── 在启动类或配置类中用 @Bean 注册 RestTemplate
│   │   ├── 注入方式：@Autowired 注入到测试类或 Service
│   │   ├── 作用：Java 代码中发送 HTTP 请求调用他人接口
│   │   └── 用 @SpringBootTest 可拉起完整容器测试
│   │
│   ├── 知识点 408：RestTemplate 请求 GET 服务
│   │   ├── getForObject(url, 返回值类型.class)：直接拿响应体
│   │   ├── getForEntity(url, 返回值类型.class)：拿响应体+响应头+状态码
│   │   └── 示例：restTemplate.getForObject("http://localhost:8080/api/books", Result.class)
│   │
│   ├── 知识点 409：RestTemplate 请求 POST 服务
│   │   ├── postForObject(url, 请求体对象, 返回值类型.class)
│   │   ├── postForEntity(...)：带状态码版本
│   │   ├── 请求体需用 HttpEntity 包装，或直接传对象
│   │   └── 示例：restTemplate.postForObject(url, new Book(...), Result.class)
│   │
│   ├── 知识点 410：RestTemplate 请求 DELETE 服务
│   │   ├── delete(url, urlVariables)：无返回值
│   │   └── 示例：restTemplate.delete("http://localhost:8080/api/books/{id}", 1)
│   │
│   ├── 知识点 411：使用 exchange 请求 DELETE 服务
│   │   ├── exchange(url, HttpMethod, HttpEntity, 返回值类型.class)
│   │   ├── 通用方法：可替代所有 GET/POST/PUT/DELETE
│   │   ├── 灵活性：可自定义请求头和请求体
│   │   └── 示例：restTemplate.exchange(url, HttpMethod.DELETE, entity, Result.class)
│   │
│   └── 知识点 412：RestTemplate 请求总结
│       ├── getForObject / getForEntity → GET
│       ├── postForObject / postForEntity → POST
│       ├── put → PUT
│       ├── delete → DELETE
│       └── exchange → 万能方法，推荐使用
│
├── 模块二：使用 Java 类方式注入 Bean 到 IOC（413）
│   │
│   └── 知识点 413：使用 Java 类方式注入 Bean 到 IOC
│       ├── @Component + @ConfigurationProperties 将配置映射为 Bean
│       ├── @Service / @Controller / @Repository 分层注解
│       ├── @Bean 在 @Configuration 类中手动注册组件
│       └── 与项目一的 BookConfig 配合，将 yml 配置注入为 Bean
│
├── 模块三：Swagger 接口文档（414-416）
│   │
│   ├── 知识点 414：整合 Swagger - 环境搭建
│   │   ├── 依赖引入：springfox-boot-starter
│   │   ├── 编写 SwaggerConfig 配置类
│   │   ├── @EnableSwagger2 开启 Swagger
│   │   ├── Docket Bean：扫描指定包（com.chilly.demo.controller）
│   │   ├── 文档类型：DocumentationType.SWAGGER_2
│   │   ├── ApiInfo：标题、描述、版本、联系人
│   │   ├── securitySchemes + securityContexts 配置 Authorization Header
│   │   └── 访问路径：http://localhost:8080/swagger-ui/index.html
│   │
│   ├── 知识点 415：整合 Swagger - 注解详解
│   │   ├── @Api(tags = "模块名")：标注 Controller 类
│   │   ├── @ApiOperation(value = "接口描述")：标注方法
│   │   ├── @ApiParam("参数说明")：标注参数
│   │   ├── @ApiModel("实体描述")：标注实体类
│   │   └── @ApiModelProperty("字段描述")：标注实体字段
│   │
│   └── 知识点 416：整合 Swagger - 注意事项
│       ├── SpringBoot 2.6+ 必须配置 spring.mvc.pathmatch.matching-strategy=ant_path_matcher
│       ├── 拦截器需放行 /swagger-ui/**、/v3/api-docs/** 等路径
│       ├── 返回类型用泛型 Result<T> 时，Swagger 能正确识别 data 类型
│       ├── ApiKey 的 name 必须与 SecurityReference 的引用名一致
│       └── URL 参数 ?authorization=Authorization%20admin-token 可预填 Token
│
├── 模块四：SpringBoot 整合 AOP（417）
│   │
│   └── 知识点 417：SpringBoot 整合 AOP
│       ├── 依赖引入：spring-boot-starter-aop
│       ├── 核心概念：
│       │   ├── 切面（Aspect）：@Aspect + @Component 标记的类
│       │   ├── 切点（Pointcut）：@Pointcut("execution(...)") 定义规则
│       │   ├── 通知（Advice）：@Around / @Before / @After 标记方法
│       │   └── 连接点（JoinPoint）：ProceedingJoinPoint 获取方法信息
│       ├── 切点表达式：
│       │   └── execution(public * com.chilly.demo.controller..*.*(..))
│       │       ├── public：修饰符
│       │       ├── * 返回值：任意类型
│       │       ├── com.chilly.demo.controller..*：包及其子包
│       │       ├── .*(..)：任意方法名、任意参数
│       │       └── 含义：拦截 controller 包下所有公开方法
│       ├── @Pointcut 方法体必须为空（只做名称标签）
│       ├── @Around 环绕通知：
│       │   ├── 入参：ProceedingJoinPoint
│       │   ├── 执行前：打印请求 URL、方法、参数、IP
│       │   ├── 执行：Object result = joinPoint.proceed()
│       │   ├── 执行后：打印返回结果、耗时
│       │   └── 最终 return result
│       ├── 与 Controller 日志的关系：AOP 与 @Slf4j 互相独立，互不干扰
│       └── 不加 spring-boot-starter-aop 则 @Aspect 不生效
│
├── 模块五：SpringBoot 整合拦截器（418）
│   │
│   └── 知识点 418：SpringBoot 整合拦截器
│       ├── 实现步骤：
│       │   ├── ① 写类实现 HandlerInterceptor 接口
│       │   ├── ② 重写 preHandle() 方法（执行前拦截）
│       │   ├── ③ 用 @Component 交给 Spring 管理
│       │   └── ④ 在 WebMvcConfig 中 registry.addInterceptor() 注册
│       ├── TokenInterceptor 校验逻辑：
│       │   ├── request.getHeader("Authorization") 从请求头取 token
│       │   ├── 与 VALID_TOKEN = "admin-token" 比较
│       │   ├── 成功：return true 放行
│       │   ├── 失败：手动设置状态码 401，返回 JSON 错误信息
│       │   └── 失败必须 return false，阻止请求继续
│       ├── WebMvcConfig 注册：
│       │   ├── @Configuration + 实现 WebMvcConfigurer
│       │   ├── @Autowired TokenInterceptor
│       │   ├── addPathPatterns("/api/**")：拦截哪些路径
│       │   └── excludePathPatterns(swagger静态资源)：放行哪些路径
│       ├── 关键注意点：
│       │   ├── 仅有 @Component 不会生效，必须注册
│       │   ├── 多个拦截器按注册顺序执行
│       │   └── 拦截器配置在 WebMvcConfigurer 中
│       └── 拦截器与过滤器的区别：
│           ├── 拦截器：Spring MVC 层（HandlerInterceptor）
│           └── 过滤器：Servlet 容器层（Filter）
│
└── 模块六：Swagger + AOP + 拦截器使用总结（419）
    │
    └── 知识点 419：Swagger + AOP + 拦截器使用总结
        ├── 分工定位：
        │   ├── Swagger：生成接口文档，便于调试
        │   ├── AOP：横切关注点，统一记录请求日志
        │   └── 拦截器：前置鉴权，统一校验 Token
        ├── 调用链路（一次请求的完整流程）：
        │   ├── 客户端请求 → 拦截器 preHandle() 校验 Token
        │   │   ├── 校验失败 → 返回 401，请求终止
        │   │   └── 校验成功 → 继续
        │   ├── → AOP @Around 前置日志
        │   ├── → Controller 方法执行（@Slf4j 日志）
        │   ├── → AOP @Around 后置日志
        │   └── → 响应返回客户端
        ├── 配置要点：
        │   ├── 拦截器必须放行 Swagger 资源路径
        │   ├── Swagger 安全配置与拦截器 Header 名称保持一致
        │   └── AOP 切点表达式精准定位 controller 包
        └── 三者互不干扰，各司其职