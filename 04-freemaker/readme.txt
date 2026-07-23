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

======

项目目录：
项目四：图书管理·前端交互版 完整目录
│
├── pom.xml [已修改]
│   └── (新增 freemarker, 排除 tomcat 引入 jetty, 保留 mysql/mybatis/druid/pagehelper/jjwt)
│
├── src/
│   ├── main/
│   │   ├── java/com/chilly/demo/
│   │   │   ├── DemoApplication.java [已修改] (新增 ServletRegistrationBean)
│   │   │   ├── common/Result.java [未变]
│   │   │   ├── config/
│   │   │   │   ├── SwaggerConfig.java [未变]
│   │   │   │   ├── WebMvcConfig.java [已修改] (新增 addCorsMappings)
│   │   │   │   └── BookConfig.java [废弃/未使用]
│   │   │   ├── controller/
│   │   │   │   ├── BookController.java [未变] (REST API)
│   │   │   │   └── PageController.java [新增] (页面跳转)
│   │   │   ├── service/
│   │   │   │   ├── BookService.java [未变]
│   │   │   │   └── BookRestClientService.java [未变]
│   │   │   ├── mapper/BookMapper.java [未变]
│   │   │   ├── model/Book.java [未变]
│   │   │   ├── interceptor/TokenInterceptor.java [未变]
│   │   │   ├── aspect/LogAspect.java [未变]
│   │   │   ├── util/JwtUtil.java [未变]
│   │   │   └── servlet/CustomServlet.java [新增] (自定义 Servlet)
│   │   └── resources/
│   │       ├── application.yml [已修改] (新增 freemarker & jackson 配置)
│   │       ├── application-dev.yml [未变]
│   │       ├── application-test.yml [未变]
│   │       ├── logback-spring.xml [未变]
│   │       ├── mapper/BookMapper.xml [未变]
│   │       └── templates/
│   │           └── bookList.ftl [新增] (主页面)
│   └── test/... [未变]

修改步骤：
第 1 步：引入 FreeMarker 依赖（修改 pom.xml）
第 2 步：配置 FreeMarker（修改 application.yml）
第 3 步：新建页面控制器 PageController.java（新增文件）
第 4 步：创建 FreeMarker 页面 src/main/resources/templates/bookList.ftl（新增文件）
第 5 步：配置 CORS 跨域，在 WebMvcConfig.java 中追加跨域配置
第 6 步：Json 格式优化（在 application.yml 中追加 Jackson 配置）
第 7 步：切换嵌入式 Servlet 容器（Tomcat → Jetty）
在 pom.xml 中排除 Tomcat，引入 Jetty
第 8 步：注册自定义 Servlet（非 Controller 方式）
1. 在servlet目录，新建 CustomServlet.java
2. 在 启动类中注册（使用 ServletRegistrationBean）
启动项目后查看：http://localhost:8080/custom-servlet

运行：
run.sh

查看首页：
http://localhost:8080/index

查看Servlet返回：
http://localhost:8080/custom-servlet

文档：
http://localhost:8080/swagger-ui/index.html

知识点整理：

项目四：图书管理·前端交互版（基于项目三改造）
│
├── 一、FreeMarker 模板引擎（420）
│   ├── 知识点 420：SpringBoot 整合 FreeMarker 开发页面
│   │   ├── 依赖引入：spring-boot-starter-freemarker
│   │   ├── 配置：application.yml 中配置 spring.freemarker 后缀、字符集、模板路径
│   │   ├── 模板文件：.ftl 格式（FreeMarker Template Language）
│   │   │   ├── 本质：在静态 HTML 中嵌入动态占位符（${变量}）
│   │   │   ├── 渲染流程：服务器端执行 → 替换占位符 → 输出纯 HTML 到浏览器
│   │   │   └── 与静态 HTML 的区别：必须经过后端 FreeMarker 引擎处理
│   │   ├── 控制器：PageController（@Controller）
│   │   │   ├── 职责：返回视图名称（如 "bookList"），由 FreeMarker 解析为 .ftl 文件
│   │   │   └── 与 @RestController 的区别：返回 HTML 页面 vs 返回 JSON 数据
│   │   ├── 数据传递：Model.addAttribute("books", list) → 模板中 ${books} 读取
│   │   ├── FTL 常用指令：
│   │   │   ├── <#if>：条件判断
│   │   │   ├── <#list>：循环遍历集合
│   │   │   ├── ${变量}：输出变量值
│   │   │   └── <#include>：引入其他模板
│   │   └── 关键注意点：FreeMarker 依赖 JavaBean 的 getter 方法（Lombok 冲突时需手动生成）
│   │
│   ├── 知识点扩展：FreeMarker 与 JavaScript 模板字符串的冲突
│   │   ├── 原因：FreeMarker 的 ${} 与 ES6 模板字符串的 ${} 语法完全一致
│   │   ├── 解决方案：
│   │   │   ├── 方案一：转义 → \${book.id}（告诉 FreeMarker 跳过解析）
│   │   │   ├── 方案二：字符串拼接 → '<td>' + book.id + '</td>'（避免冲突）
│   │   │   └── 方案三：使用 <#ftl output_format="HTML"> 指令（不推荐）
│   │   └── 项目选择：采用字符串拼接，完全绕过模板字符串（稳）
│   │
│   └── 双轨制架构（API + 页面分离）
│       ├── BookController（@RestController）→ /api/books → 返回 JSON
│       ├── PageController（@Controller）→ /index → 返回 HTML
│       └── 优势：页面和 API 解耦，前端可独立演进
│
├── 二、跨域 CORS 解决方案（421-422）
│   ├── 知识点 421/422：SpringBoot 解决 CORS 跨域问题
│   │   ├── 什么是跨域：前端域名/端口/协议与后端不一致时，浏览器拦截请求
│   │   ├── 场景判断：本项目（FreeMarker 页面与 API 同端口）不存在跨域
│   │   │   └── 但为了兼容前后端分离部署，依然配置 CORS
│   │   ├── 核心配置：在 WebMvcConfig 中重写 addCorsMappings 方法
│   │   ├── 关键配置项：
│   │   │   ├── allowedOrigins（Spring 5.3 前）
│   │   │   │   ├── 作用：指定允许的来源（如 "http://localhost:3000"）
│   │   │   │   └── 限制：与 allowCredentials(true) 冲突时不能写 "*"
│   │   │   ├── allowedOriginPatterns（Spring 5.3+ 推荐）
│   │   │   │   ├── 作用：支持通配符模式（如 "*"、"http://localhost:*"）
│   │   │   │   └── 兼容 allowCredentials(true)：无冲突
│   │   │   ├── allowedMethods：允许的 HTTP 方法（GET/POST/PUT/DELETE/OPTIONS）
│   │   │   ├── allowedHeaders：允许的请求头（如 Authorization）
│   │   │   ├── allowCredentials：是否允许携带凭证（Cookie / Authorization）
│   │   │   └── maxAge：预检请求（OPTIONS）的有效缓存时间（秒）
│   │   ├── 常见错误：
│   │   │   └── "When allowCredentials is true, allowedOrigins cannot contain '*'"
│   │   │       └── 原因：浏览器强制要求带凭证时 Origin 必须是具体值
│   │   │       └── 解决：使用 allowedOriginPatterns("*") 替代 allowedOrigins("*")
│   │   └── 安全建议：开发环境用 *，生产环境必须写具体域名白名单
│   │
│   └── CORS 流程回顾（可选）
│       ├── 简单请求：直接发请求，后端通过 CORS 头判断
│       └── 预检请求（非简单请求）：先发 OPTIONS，再发实际请求
│
├── 三、JSON 使用技巧（423）
│   ├── 知识点 423：SpringBoot 中 Json 使用技巧
│   │   ├── 默认框架：Jackson（spring-boot-starter-web 自带）
│   │   ├── 常用注解：
│   │   │   ├── @JsonIgnore：忽略某个字段（不序列化）
│   │   │   ├── @JsonProperty：指定序列化后的字段名（如 "bookName"）
│   │   │   ├── @JsonFormat：格式化日期（如 "yyyy-MM-dd HH:mm:ss"）
│   │   │   └── @JsonInclude：控制序列化条件（如 NON_NULL）
│   │   ├── 配置优化（application.yml）：
│   │   │   ├── spring.jackson.date-format：全局日期格式
│   │   │   ├── spring.jackson.time-zone：时区设置
│   │   │   └── spring.jackson.serialization.write-dates-as-timestamps：关闭时间戳
│   │   ├── 前端接收：$.ajax success 回调中的 res 已是 JSON 对象
│   │   └── 常见应用：
│   │       ├── 前端日期格式化（避免显示时间戳）
│   │       └── 字段名映射（数据库下划线 → Java 驼峰 → 前端驼峰）
│   │
│   └── 扩展：前端 JSON 处理
│       ├── JSON.stringify()：JS 对象 → JSON 字符串（发送数据）
│       └── JSON.parse()：JSON 字符串 → JS 对象（接收数据）【$.ajax 自动完成】
│
├── 四、切换嵌入式 Servlet 容器（424）
│   ├── 知识点 424：切换嵌入式 Servlet 容器
│   │   ├── SpringBoot 默认容器：Tomcat（最常用、最成熟）
│   │   ├── 替代方案：
│   │   │   ├── Jetty：轻量级、适合嵌入式开发（项目四选择）
│   │   │   └── Undertow：高性能、非阻塞（Red Hat 出品）
│   │   ├── 切换步骤（Tomcat → Jetty）：
│   │   │   ├── ① 在 spring-boot-starter-web 中排除 spring-boot-starter-tomcat
│   │   │   └── ② 引入 spring-boot-starter-jetty
│   │   ├── 验证方式：启动日志显示 "Jetty started on port(s) 8080"
│   │   └── 对比分析：
│   │       ├── Tomcat：成熟稳定、生态丰富、默认
│   │       ├── Jetty：轻量级、启动快、适合开发调试
│   │       └── Undertow：高性能、适合高并发场景
│   │
│   └── 选择建议：普通项目用 Tomcat 即可；嵌入式/资源受限场景用 Jetty
│
├── 五、注册 Servlet 组件（425-426）
│   ├── 知识点 425：注解方式注册 Servlet 组件
│   │   ├── @WebServlet：在 Servlet 类上标注（需配合 @ServletComponentScan）
│   │   ├── @WebFilter：注册过滤器
│   │   └── @WebListener：注册监听器
│   │
│   ├── 知识点 426：注册器方式注册 Servlet 组件（项目四使用）
│   │   ├── 方式：使用 ServletRegistrationBean 手动注册
│   │   ├── 步骤：
│   │   │   ├── ① 新建继承 HttpServlet 的类（CustomServlet）
│   │   │   ├── ② 重写 doGet / doPost 方法
│   │   │   └── ③ 在 @Configuration 类中创建 @Bean：ServletRegistrationBean
│   │   ├── 配置项：
│   │   │   ├── setLoadOnStartup(1)：启动时加载（数字越小越优先）
│   │   │   └── addUrlMappings("/custom-servlet")：访问路径
│   │   ├── 与 @RestController 的区别：
│   │   │   ├── Servlet：底层 API，无自动 JSON 序列化，需手动写响应
│   │   │   ├── @RestController：高级抽象，自动解析参数、返回 JSON
│   │   │   └── Servlet 更灵活，但开发效率低（需要手动处理 IO）
│   │   └── 适用场景：集成第三方 Servlet 组件、低级 HTTP 处理
│   │
│   └── 对比总结
│       ├── 注解方式（@WebServlet）：简单，适合单一 Servlet
│       └── 注册器方式（ServletRegistrationBean）：灵活，适合外部配置
│
├── 六、前后端完整交互流程
│   ├── 调用链路
│   │   ├── 1. 浏览器访问 /index → PageController 返回 bookList.ftl
│   │   ├── 2. FreeMarker 引擎渲染模板 → 输出含 CSS/JS 的 HTML
│   │   ├── 3. HTML 加载后，JavaScript 执行 loadBooks()
│   │   ├── 4. Ajax 请求 GET /api/books（带 JWT）
│   │   ├── 5. TokenInterceptor 校验 JWT → 放行
│   │   ├── 6. BookController.list() → Service → Mapper → MySQL
│   │   ├── 7. 返回 Result<List<Book>> → Jackson 序列化为 JSON
│   │   ├── 8. Ajax success 回调接收到 JSON → JavaScript 动态渲染表格
│   │   └── 9. 浏览器显示完整图书列表
│   │
│   └── 前端功能模块
│       ├── 页面加载：自动显示列表
│       ├── 新增图书：表单输入 + POST 请求 + 刷新列表
│       ├── 删除图书：确认弹窗 + DELETE 请求 + 刷新列表
│       └── 认证：所有请求携带 Authorization: Bearer <token>
│
├── 七、文件改动清单（对比项目三）
│   ├── 新增文件（3个）
│   │   ├── src/main/java/com/chilly/demo/controller/PageController.java
│   │   ├── src/main/resources/templates/bookList.ftl
│   │   └── src/main/java/com/chilly/demo/servlet/CustomServlet.java
│   │
│   ├── 修改文件（4个）
│   │   ├── pom.xml（新增 FreeMarker、排除 Tomcat、引入 Jetty）
│   │   ├── application.yml（新增 freemarker、jackson 配置）
│   │   ├── WebMvcConfig.java（新增 addCorsMappings）
│   │   └── DemoApplication.java（新增 ServletRegistrationBean）
│   │
│   └── 不变文件
│       ├── BookController.java（API 层完全保留）
│       ├── BookService.java / BookMapper.java（业务层不变）
│       ├── TokenInterceptor.java / JwtUtil.java（认证不变）
│       ├── LogAspect.java（AOP 日志不变）
│       └── SwaggerConfig.java（API 文档不变）
│
└── 八、知识点覆盖总览（420-426）
    ├── 420：SpringBoot 整合 FreeMarker 开发页面 ✅
    ├── 421-422：SpringBoot 解决 CORS 跨域问题 ✅
    ├── 423：SpringBoot 中 Json 使用技巧 ✅
    ├── 424：切换嵌入式 Servlet 容器 ✅
    ├── 425：注解方式注册 Servlet 组件 ✅
    └── 426：注册器方式注册 Servlet 组件 ✅
        └── 合计：7 个知识点全部覆盖（421/422 合并为 CORS 模块）