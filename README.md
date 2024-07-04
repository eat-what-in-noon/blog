### 项目名称：简单博客平台

#### 项目简介
一个用户可以发布、编辑和删除博客文章，查看他人文章并发表评论的简单博客平台。添加文章标签和分类功能，方便文章的组织和检索。

#### 功能需求
1. **用户注册和登录**：
   - 用户可以注册新账号并登录。
   - 实现用户认证和授权。

2. **文章管理**：
   - 用户可以发布新文章。
   - 用户可以编辑和删除自己的文章。
   - 用户可以查看他人的文章。

3. **评论功能**：
   - 用户可以在文章下发表评论。
   - 用户可以编辑和删除自己的评论。

4. **文章标签和分类**：
   - 用户可以给文章添加标签和分类。
   - 用户可以通过标签和分类浏览文章。

5. **文章检索**：
   - 用户可以通过关键词搜索文章。

#### 技术栈(仅供参考)
1. **Spring Boot** - 后端框架
2. **Spring Security** - 用户认证和授权
3. **Spring Data JPA** - 数据持久化
4. **element** - 前端模板引擎
5. **Mysql** - 内存数据库，用于开发和测试
6. **Vue** - 前端样式

#### 后端代码结构
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── config/
│   │           │   └── SecurityConfig.java        # Spring Security配置类
│   │           ├── controller/
│   │           │   ├── ArticleController.java     # 文章控制器
│   │           │   ├── CommentController.java     # 评论控制器
│   │           │   └── UserController.java        # 用户控制器
│   │           ├── entity/
│   │           │   ├── Article.java               # 文章实体类
│   │           │   ├── Comment.java               # 评论实体类
│   │           │   ├── Tag.java                   # 标签实体类
│   │           │   ├── Category.java              # 分类实体类
│   │           │   └── User.java                  # 用户实体类
│   │           ├── repository/
│   │           │   ├── ArticleRepository.java     # 文章仓库接口
│   │           │   ├── CommentRepository.java     # 评论仓库接口
│   │           │   ├── TagRepository.java         # 标签仓库接口
│   │           │   ├── CategoryRepository.java    # 分类仓库接口
│   │           │   └── UserRepository.java        # 用户仓库接口
│   │           ├── service/
│   │           │   ├── ArticleService.java        # 文章服务接口
│   │           │   ├── CommentService.java        # 评论服务接口
│   │           │   ├── TagService.java            # 标签服务接口
│   │           │   ├── CategoryService.java       # 分类服务接口
│   │           │   └── UserService.java           # 用户服务接口
│   │           └── BlogApplication.java           # Spring Boot 应用入口类
│   └── resources/
│       ├── application.properties                # 应用配置文件，包括数据库连接等配置
│       └── static/
│           └── (optional)                        # 可选的静态资源文件夹，如图片、CSS等
└── test/
    └── java/
        └── com/
            └── example/
                └── BlogApplicationTests.java      # 测试类，用于测试应用功能

```


前端（Vue.js）
```
frontend/
├── public/
│   └── index.html                               # 主页 HTML 文件
└── src/
    ├── assets/                                  # 资源文件夹，如图片、样式表等
    ├── components/                              # 组件文件夹，存放Vue组件
    │   ├── ArticleList.vue                      # 文章列表组件
    │   ├── ArticleForm.vue                      # 文章表单组件
    │   ├── CommentSection.vue                   # 评论区组件
    │   ├── TagList.vue                          # 标签列表组件
    │   └── CategoryList.vue                     # 分类列表组件
    ├── services/                                # 服务文件夹，用于与后端API通信
    │   ├── ArticleService.js                    # 文章相关服务
    │   ├── CommentService.js                    # 评论相关服务
    │   ├── TagService.js                        # 标签相关服务
    │   └── CategoryService.js                   # 分类相关服务
    ├── views/                                   # 视图文件夹，存放页面视图
    │   ├── Home.vue                             # 主页视图
    │   ├── Login.vue                            # 登录视图
    │   └── Register.vue                         # 注册视图
    ├── App.vue                                  # 根组件
    └── main.js                                  # Vue.js 应用入口文件
```


在提供的数据库表结构中，以下是每个字段的功能和作用说明：

### 用户表 (`users`)

- **id**: 用户表的主键，使用 `BIGINT` 类型，自增长，用于唯一标识每个用户。
- **username**: 用户名，`VARCHAR(20)`，不能为空，用于用户登录和显示。
- **PASSWORD**: 密码，`VARCHAR(20)`，不能为空，存储用户密码的哈希或加密形式。
- **ROLE**: 用户角色，`VARCHAR(10)`，表示用户在系统中的权限级别或角色。
- **provider**: 第三方登录提供者，`VARCHAR(20)`，存储使用第三方登录时的提供者名称（如GitHub、Google）。
- **provider_id**: 第三方登录提供者的用户ID，`VARCHAR(100)`，存储第三方登录提供者分配的唯一标识符。
- **email**: 邮箱，`VARCHAR(40)`，唯一，用于用户的电子邮件地址。
- **phone_number**: 手机号码，`VARCHAR(20)`，可选的，用于用户的联系方式。
- **gender**: 性别，`VARCHAR(10)`，可选的，存储用户的性别信息。
- **introduction**: 简介，`TEXT`，可选的，用于存储用户的个人简介或描述。

### 标签表 (`tags`)

- **id**: 标签表的主键，使用 `BIGINT` 类型，自增长，用于唯一标识每个标签。
- **NAME**: 标签名，`VARCHAR(50)`，不能为空且唯一，用于标识不同的标签。

### 分类表 (`categories`)

- **id**: 分类表的主键，使用 `BIGINT` 类型，自增长，用于唯一标识每个分类。
- **NAME**: 分类名，`VARCHAR(50)`，不能为空且唯一，用于标识不同的分类。

### 文章表 (`articles`)

- **id**: 文章表的主键，使用 `BIGINT` 类型，自增长，用于唯一标识每篇文章。
- **title**: 文章标题，`VARCHAR(255)`，不能为空，存储文章的标题信息。
- **content**: 文章内容，`TEXT`，不能为空，存储文章的具体内容。
- **created_date**: 创建日期，`TIMESTAMP`，默认为当前时间戳，记录文章的创建时间。
- **author_id**: 作者ID，`BIGINT`，外键，关联到 `users` 表的 `id` 字段，表示文章的作者。
- **category_id**: 分类ID，`BIGINT`，外键，关联到 `categories` 表的 `id` 字段，表示文章所属的分类。

### 评论表 (`comments`)

- **id**: 评论表的主键，使用 `BIGINT` 类型，自增长，用于唯一标识每条评论。
- **content**: 评论内容，`TEXT`，不能为空，存储评论的具体内容。
- **created_date**: 创建日期，`TIMESTAMP`，默认为当前时间戳，记录评论的创建时间。
- **author_id**: 作者ID，`BIGINT`，外键，关联到 `users` 表的 `id` 字段，表示评论的作者。
- **article_id**: 文章ID，`BIGINT`，外键，关联到 `articles` 表的 `id` 字段，表示评论所属的文章。

### 文章标签关联表 (`article_tags`)

- **article_id**: 文章ID，`BIGINT`，外键，关联到 `articles` 表的 `id` 字段，表示文章与标签的关联。
- **tag_id**: 标签ID，`BIGINT`，外键，关联到 `tags` 表的 `id` 字段，表示文章所拥有的标签。


#### 以下内容暂未确定，仅供参考

#### 实现步骤

1. **项目初始化**
   - 使用Spring Initializr创建一个新的Spring Boot项目，选择所需的依赖项（Spring Web、Spring Security、Spring Data JPA、Thymeleaf、H2 Database）。

2. **配置文件**
   - 在`application.properties`中配置数据库、Spring Security等信息。

3. **用户管理**
   - 创建用户实体类和用户仓库类。
   - 使用Spring Security实现用户注册、登录和角色管理。

4. **文章管理**
   - 创建文章实体类和文章仓库类。
   - 实现文章的创建、编辑、删除和查看功能。

5. **评论功能**
   - 创建评论实体类和评论仓库类。
   - 实现评论的添加、编辑和删除功能。

6. **标签和分类**
   - 创建标签和分类实体类及其仓库类。
   - 实现文章标签和分类的添加和检索功能。

7. **前端界面**
   - 使用Thymeleaf和Bootstrap创建用户友好的前端界面，包括注册、登录、文章列表、文章表单、评论区、标签和分类浏览页面。

8. **文章检索**
   - 实现文章的关键词搜索功能。

