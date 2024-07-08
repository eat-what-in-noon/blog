
CREATE DATABASE blog;
USE blog;

-- 用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10),
    provider VARCHAR(20),
    provider_id VARCHAR(100),
    email VARCHAR(40) UNIQUE,
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    introduction TEXT,
    UNIQUE(provider, provider_id)
);


-- 标签表
CREATE TABLE tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(50) NOT NULL UNIQUE
);


-- 分类表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO category (id, category_name) VALUES
(1, '计算机技术类'),
(2, '生活类'),
(3, '娱乐类'),
(4, '商业与经济类'),
(5, '教育类'),
(6, '艺术与文化类'),
(7, '科学与技术类'),
(8, '社会与政治类'),
(9, '心理学与自我提升'),
(10, '手工艺与DIY'),
(11, '环境与可持续发展'),
(12, '宠物与动物'),
(13, '汽车与交通'),
(14, '摄影与视频制作'),
(15, '科技与创新'),
(16, '社会活动与公益'),
(17, '语言与文化'),
(18, '婚礼与庆典'),
(19, '居家生活'),
(20, '游戏与电子竞技');


-- 文章表
CREATE TABLE article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT,
    category_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES user(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);


-- 评论表
CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT,
    article_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES user(id),
    FOREIGN KEY (article_id) REFERENCES article(id)
);


-- 文章标签关联表
CREATE TABLE article_tag (
    article_id BIGINT,
    tag_id BIGINT,
    FOREIGN KEY (article_id) REFERENCES article(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id),
    PRIMARY KEY (article_id, tag_id)
);




-- user 表存储用户信息，包括用户名、密码和角色。
-- article 表存储博客文章信息，包括标题、内容、创建日期、作者ID和分类ID。
-- comment 表存储评论信息，包括内容、创建日期、作者ID和文章ID。
-- tag 表存储标签信息，包括标签名。
-- categories 表存储分类信息，包括分类名。
-- article_tags 表存储文章与标签的多对多关系。


