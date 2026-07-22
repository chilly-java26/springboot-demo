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

======

项目目录：
项目三：图书管理·数据持久版（基于项目二改造）
│
├── 📁 src/main/java/com/chilly/demo/
│   │
│   ├── 📄 DemoApplication.java                    【不变】
│   │
│   ├── 📁 common/
│   │   └── 📄 Result.java                         【不变】
│   │
│   ├── 📁 config/
│   │   ├── 📄 SwaggerConfig.java                  【不变】
│   │   ├── 📄 WebMvcConfig.java                   【不变】
│   │   └── 📄 BookConfig.java                     【废弃】（改为数据库读取）
│   │
│   ├── 📁 controller/
│   │   └── 📄 BookController.java                 【修改】（新增分页接口 /page）
│   │
│   ├── 📁 interceptor/
│   │   └── 📄 TokenInterceptor.java               【不变】
│   │
│   ├── 📁 aspect/
│   │   └── 📄 LogAspect.java                      【不变】
│   │
│   ├── 📁 service/
│   │   └── 📄 BookService.java                    【大幅修改】（内存Map → Mapper）
│   │
│   ├── 📁 mapper/                                 【新增包】
│   │   └── 📄 BookMapper.java                     【新增文件】
│   │
│   ├── 📁 util/
│   │   └── 📄 JwtUtil.java                        【不变】
│   │
│   └── 📁 model/
│       └── 📄 Book.java                           【不变】
│
├── 📁 src/main/resources/
│   │
│   ├── 📄 application.yml                         【大幅修改】（数据源 + MyBatis + PageHelper）
│   ├── 📄 application-dev.yml                     【不变】
│   ├── 📄 application-test.yml                    【不变】
│   ├── 📄 logback-spring.xml                      【不变】
│   │
│   └── 📁 mapper/                                 【新增目录】
│       └── 📄 BookMapper.xml                      【新增文件】
│
└── 📄 pom.xml                                     【修改】（新增 4 个依赖）

修改步骤：
// 修改配置
0. 环境准备（MySQL 建表）：scripts/init_db.sql
1. 更新pom文件：新增mysql driver, mybatis, druid, pageHelper依赖
2. 修改 application.yml（替换为数据库配置）：druid（数据源），mybatis，pageHelper
// 添加数据库接口
3. 新增 mapper/BookMapper.java：DAO 层接口
4. 新建 BookMapper.xml：SQL 映射文件
5. 改造 BookService.java：
删除：Map、AtomicInteger、@PostConstruct 初始化。
新增：注入 BookMapper，所有方法改为调用 Mapper。
// 支持分页
6. 增加分页查询：PageHelper（BookService, BookController）

运行：
run.sh

获取token：
scripts/jwt-gen.sh

文档：
http://localhost:8080/swagger-ui/index.html

知识点整理：
项目三：图书管理·数据持久版
│
├── 模块一：MyBatis 整合（428-436）
│   │
│   ├── 知识点 428：逆向工程插件
│   │   ├── 概念：根据数据库表自动生成实体类、Mapper 接口、XML 映射文件
│   │   ├── 插件：mybatis-generator-maven-plugin
│   │   ├── 配置文件：generatorConfig.xml（指定表名、包路径、目标目录）
│   │   └── 项目三不强制使用（手写 Mapper 更利于学习）
│   │
│   ├── 知识点 429：扫描配置
│   │   ├── @Mapper 注解：标记 Mapper 接口，让 Spring 扫描并生成代理对象
│   │   ├── @MapperScan：在启动类或配置类上批量扫描包路径
│   │   └── 项目三使用 @Mapper 注解（显式标记每个接口）
│   │
│   ├── 知识点 430：别名配置
│   │   ├── 作用：简化 XML 中的 resultType 写法（不用写全限定类名）
│   │   ├── 配置方式：mybatis.type-aliases-package: com.chilly.demo.model
│   │   └── 效果：XML 中可以直接写 Book，不用写 com.chilly.demo.model.Book
│   │
│   ├── 知识点 431：打印 SQL
│   │   ├── 作用：开发阶段查看 MyBatis 生成的 SQL 语句，便于调试
│   │   ├── 配置方式：mybatis.configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
│   │   └── 生产环境建议关闭（或改为 logback 输出到文件）
│   │
│   ├── 知识点 432：全局配置文件
│   │   ├── 作用：MyBatis 核心配置，控制行为（缓存、执行器、自动映射等）
│   │   ├── 配置内容：map-underscore-to-camel-case、cache-enabled、lazy-loading-enabled
│   │   └── 在 SpringBoot 中通过 application.yml 的 mybatis.configuration 节点配置
│   │
│   ├── 知识点 433：集成分页插件
│   │   ├── 依赖：pagehelper-spring-boot-starter
│   │   ├── 使用方式：PageHelper.startPage(pageNum, pageSize) 紧跟在查询前
│   │   ├── 结果封装：PageInfo<Book>（包含 total、list、pages、navigatePages 等）
│   │   └── 配置：pagehelper.helper-dialect=mysql（指定数据库方言）
│   │
│   ├── 知识点 434：整合事务
│   │   ├── 注解：@Transactional（Spring 提供）
│   │   ├── 作用：保证多个 SQL 操作要么全部成功，要么全部回滚
│   │   ├── 使用位置：Service 层方法上（addBook、updateBook、deleteBook）
│   │   └── 隔离级别/传播行为：默认即可，复杂场景可调
│   │
│   ├── 知识点 435：整合 Druid 数据源
│   │   ├── 依赖：druid-spring-boot-starter
│   │   ├── 作用：连接池（管理数据库连接，提高性能）
│   │   ├── 配置项：initial-size、min-idle、max-active、max-wait
│   │   ├── 监控功能：内置 StatViewServlet（访问 /druid 查看连接池状态）
│   │   └── 项目三配置：application.yml 中 spring.datasource.druid 节点
│   │
│   └── 知识点 436：综合练习（模糊查询和总结）
│       ├── 模糊查询：使用 LIKE 语句 + #{keyword} 参数
│       ├── 动态 SQL：MyBatis 提供 <if>、<where>、<foreach> 等标签
│       └── 项目三目标：从内存 Map → 真实 MySQL 数据库
│
├── 模块二：核心实现细节
│   │
│   ├── BookMapper.java（DAO 层）
│   │   ├── 接口定义：selectAll、selectById、insert、update、deleteById
│   │   ├── @Mapper 标记接口
│   │   └── @Param 用于多参数传递（XML 中通过 #{参数名} 引用）
│   │
│   ├── BookMapper.xml（SQL 映射文件）
│   │   ├── namespace：必须与 Mapper 接口全限定名一致
│   │   ├── resultMap：定义数据库字段 → Java 属性的映射
│   │   ├── <select>：查询语句（id 对应方法名）
│   │   ├── <insert>：插入语句（useGeneratedKeys="true" 回填自增 ID）
│   │   ├── <update>：修改语句
│   │   └── <delete>：删除语句
│   │
│   ├── BookService.java（业务层）
│   │   ├── 删除：内存 Map + AtomicInteger
│   │   ├── 新增：注入 BookMapper
│   │   ├── 改造方法：getAllBooks → mapper.selectAll()
│   │   ├── 改造方法：addBook → mapper.insert(book)（ID 由数据库自增）
│   │   ├── 改造方法：updateBook → mapper.update(book)
│   │   ├── 改造方法：deleteBook → mapper.deleteById(id)
│   │   └── 新增方法：getBooksByPage（PageHelper + PageInfo 分页）
│   │
│   ├── application.yml（配置文件）
│   │   ├── spring.datasource：Druid 连接池配置
│   │   ├── mybatis：mapper-locations、type-aliases-package、configuration
│   │   └── pagehelper：helper-dialect、reasonable、support-methods-arguments
│   │
│   └── BookController.java（控制器层）
│       ├── 原有接口完全不变（GET /api/books、POST /api/books 等）
│       └── 新增分页接口：GET /api/books/page?pageNum=1&pageSize=5
│
├── 模块三：知识点覆盖总览（428-438）
│   ├── 428：逆向工程插件 ✅（提及，未强制使用）
│   ├── 429：扫描配置 ✅（@Mapper）
│   ├── 430：别名配置 ✅（mybatis.type-aliases-package）
│   ├── 431：打印 SQL ✅（log-impl: StdOutImpl）
│   ├── 432：全局配置文件 ✅（map-underscore-to-camel-case）
│   ├── 433：集成分页插件 ✅（PageHelper + PageInfo）
│   ├── 434：整合事务 ✅（@Transactional）
│   ├── 435：整合 Druid 数据源 ✅（Druid 连接池）
│   └── 436：综合练习 ✅（模糊查询 + 总结）
│       └── 合计：9 个知识点全部覆盖
│
└── 模块四：文件改动清单（对比项目二）
    ├── 新增文件（2个）
    │   ├── src/main/java/com/chilly/demo/mapper/BookMapper.java
    │   └── src/main/resources/mapper/BookMapper.xml
    ├── 修改文件（4个）
    │   ├── pom.xml（新增 4 个依赖）
    │   ├── BookService.java（内存 Map → Mapper）
    │   ├── BookController.java（新增分页接口）
    │   └── application.yml（数据源 + MyBatis + PageHelper）
    └── 不变文件
        ├── Controller、Interceptor、AOP、Swagger、JwtUtil 等
        └── Book.java、Result.java、TokenInterceptor 等保持不变