
CREATE DATABASE blog;
USE blog;

-- 用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(100) NOT NULL,
    ROLE VARCHAR(10),
    provider VARCHAR(20),
    providerId VARCHAR(100),
    email VARCHAR(40) UNIQUE,
    phoneNumber VARCHAR(20),
    gender VARCHAR(10),
    introduction TEXT,
    UNIQUE(provider, providerId)
);


-- 标签表
CREATE TABLE tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tagName VARCHAR(50) NOT NULL UNIQUE
);


-- 分类表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(50) NOT NULL UNIQUE
);



-- 文章表
CREATE TABLE article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    authorId BIGINT,
    categoryId BIGINT,
    FOREIGN KEY (authorId) REFERENCES user(id),
    FOREIGN KEY (categoryId) REFERENCES categorie(id)
);


-- 评论表
CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    authorId BIGINT,
    articleId BIGINT,
    FOREIGN KEY (authorId) REFERENCES user(id),
    FOREIGN KEY (articleId) REFERENCES article(id)
);


-- 文章标签关联表
CREATE TABLE article_tag (
    articleId BIGINT,
    tagId BIGINT,
    FOREIGN KEY (articleId) REFERENCES article(id),
    FOREIGN KEY (tagId) REFERENCES tag(id),
    PRIMARY KEY (articleId, tagId)
);




-- user 表存储用户信息，包括用户名、密码和角色。
-- article 表存储博客文章信息，包括标题、内容、创建日期、作者ID和分类ID。
-- comment 表存储评论信息，包括内容、创建日期、作者ID和文章ID。
-- tag 表存储标签信息，包括标签名。
-- categories 表存储分类信息，包括分类名。
-- article_tags 表存储文章与标签的多对多关系。


