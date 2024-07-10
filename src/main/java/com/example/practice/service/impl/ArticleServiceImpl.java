package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.*;
import com.example.practice.mapper.*;
import com.example.practice.service.ArticleService;
import com.example.practice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    // 定义article数据库操作变量
    @Autowired
    private ArticleMapper articleMapper;

    // 定义tag数据库操作变量
    @Autowired
    private TagMapper tagMapper;

    // 定义category数据库操作变量
    @Autowired
    private CategoryMapper categoryMapper;

    // 定义article_tag数据表操作变量
    @Autowired
    private ArticleTagMapper articleTagMapper;

    // 定义comment数据表操作变量
    @Autowired
    private CommentMapper commentMapper;

    // 定义ArticleLike数据表操作变量
    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    // 定义User数据表操作变量
    @Autowired
    private UserMapper userMapper;

    // 定义Tag功能操作变量
    @Autowired
    private TagService tagService;

    // 添加文章函数具体逻辑
    @Override
    public Map<String, Object> addArticle(Article article) {
        articleMapper.insert(article);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getId); // 根据 id 字段降序排列
        Integer articleId = articleMapper.selectList(queryWrapper).get(0).getId();
        return Map.of("error_message", "success", "data", articleId);
    }

    // 为文章添加标签函数具体逻辑
    @Override
    public Map<String, Object> addTagToArticle(String tagName, Integer articleId) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag == null) {
            tagService.addTag(tagName);
            queryWrapper.eq("tag_name", tagName);
            tag = tagMapper.selectOne(queryWrapper);
        }
        Integer tagId = tag.getId();
        ArticleTag articleTag = new ArticleTag(articleId, tagId);
        articleTagMapper.insert(articleTag);
        return Map.of("error_message", "success");
    }

    // 添加评论函数具体逻辑

    // 上传文章封面函数具体逻辑
    @Override
    public Map<String, Object> uploadCover(MultipartFile cover) throws IOException {
        String uploadPath = "C:/Users/Joker/Desktop/JavaSpace/practice/src/main/resources/static/cover/";
        String originalFilename = cover.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String savePath = uploadPath + fileName;
        File dest = new File(savePath);
        cover.transferTo(dest);
        return Map.of("error_message", "success", "data", fileName);
    }

    // 根据id获取文章信息函数具体逻辑
    @Override
    public Map<String, Object> getArticleInfoById(Integer id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Article article = articleMapper.selectOne(queryWrapper);
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("article_id", article.getId());
        articleLikeQueryWrapper.eq("article_id", article.getId());
        articleTagQueryWrapper.eq("article_id", article.getId());
        Integer commentCnt = commentMapper.selectList(commentQueryWrapper).size();
        Integer likeCnt = articleLikeMapper.selectList(articleLikeQueryWrapper).size();
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
        List<String> tagNames = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.eq("id", articleTag.getTagId());
            tagNames.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
        }
        return Map.of("error_message", "success", "data", article
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    // 根据tag获取所有相关文章的除content外所有内容
    @Override
    public Map<String, Object> getArticleInfoByTag(String tagName) {
        // 根据tagName查询tagId
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Integer tagId = tagMapper.selectOne(queryWrapper).getId();
        // 根据tagId查询articleId
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("tag_id", tagId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
        // 遍历articleId，返回所有相关文章
        List<Article> articles = new ArrayList<>();
        List<Integer> commentCnt = new ArrayList<>();
        List<Integer> likeCnt = new ArrayList<>();
        List<List<String>> tagNames = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("id", articleTag.getArticleId());
            Article article = articleMapper.selectOne(articleQueryWrapper);
            // 此时不需要content，为了节省开销，将content置为null
            article.setContent(null);
            articles.add(article);
            // 查询文章对应的点赞数和评论数以及所有标签
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleTag> articleTagQueryWrapper1 = new QueryWrapper<>();
            commentQueryWrapper.eq("article_id", article.getId());
            articleLikeQueryWrapper.eq("article_id", article.getId());
            articleTagQueryWrapper1.eq("article_id", article.getId());
            commentCnt.add(commentMapper.selectList(commentQueryWrapper).size());
            likeCnt.add(articleLikeMapper.selectList(articleLikeQueryWrapper).size());
            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagQueryWrapper1);
            List<String> stringList = new ArrayList<>();
            for (ArticleTag articleTag1 : articleTagList) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag1.getTagId());
                stringList.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
            }
            tagNames.add(stringList);
        }
        return Map.of("error_message", "success", "data", articles
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    @Override
    public Map<String, Object> getArticleInfoByCategory(String categoryName) {
        // 根据categoryName查询文章
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", categoryName);
        Integer categoryId = categoryMapper.selectOne(queryWrapper).getId();
        // 根据categoryId查询articleId
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("category_id", categoryId);
        // 遍历articleId，返回所有相关文章
        List<Article> articles = articleMapper.selectList(articleQueryWrapper);
        List<Integer> commentCnt = new ArrayList<>();
        List<Integer> likeCnt = new ArrayList<>();
        List<List<String>> tagNames = new ArrayList<>();
        for (Article article : articles) {
            article.setContent(null);
            // 查询文章对应的点赞数和评论数以及标签
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("article_id", article.getId());
            articleLikeQueryWrapper.eq("article_id", article.getId());
            articleTagQueryWrapper.eq("article_id", article.getId());
            commentCnt.add(commentMapper.selectList(commentQueryWrapper).size());
            likeCnt.add(articleLikeMapper.selectList(articleLikeQueryWrapper).size());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            List<String> stringList = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                stringList.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
            }
            tagNames.add(stringList);
        }
        return Map.of("error_message", "success", "data", articles
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    // 返回所有文章的除了content外内容函数具体逻辑
    @Override
    public Map<String, Object> getAllArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<Integer> commentCnt = new ArrayList<>();
        List<Integer> likeCnt = new ArrayList<>();
        List<List<String>> tagNames = new ArrayList<>();
        for (Article article : articles) {
            article.setContent(null);
            // 查询文章对应的点赞数和评论数以及标签
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("article_id", article.getId());
            articleLikeQueryWrapper.eq("article_id", article.getId());
            articleTagQueryWrapper.eq("article_id", article.getId());
            commentCnt.add(commentMapper.selectList(commentQueryWrapper).size());
            likeCnt.add(articleLikeMapper.selectList(articleLikeQueryWrapper).size());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            List<String> stringList = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                stringList.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
            }
            tagNames.add(stringList);
        }
        return Map.of("error_message", "success", "data", articles
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    // 获得文章所有评论的函数具体逻辑
    @Override
    public Map<String, Object> getComment(Integer id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<User> users = new ArrayList<>();
        if (comments == null) {
            return Map.of("error_message", "该文章暂时没有评论");
        }
        for (Comment comment : comments) {
            if (!Objects.equals(comment.getArticleId(), id)) {
                comments.remove(comment);
            } else {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", comment.getAuthorId());
                User user = userMapper.selectOne(userQueryWrapper);
                users.add(user);
            }
        }
        return Map.of("error_message", "success", "data", comments, "users", users);
    }

    @Override
    public Map<String, Object> findArticle(String title) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        if (articles.size() == 0) {
            return Map.of("error_message", "没有相关文章");
        }
        List<Integer> commentCnt = new ArrayList<>();
        List<Integer> likeCnt = new ArrayList<>();
        List<List<String>> tagNames = new ArrayList<>();
        for (Article article : articles) {
            article.setContent(null);
            // 查询文章对应的点赞数和评论数以及标签
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("article_id", article.getId());
            articleLikeQueryWrapper.eq("article_id", article.getId());
            articleTagQueryWrapper.eq("article_id", article.getId());
            commentCnt.add(commentMapper.selectList(commentQueryWrapper).size());
            likeCnt.add(articleLikeMapper.selectList(articleLikeQueryWrapper).size());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            List<String> stringList = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                stringList.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
            }
            tagNames.add(stringList);
        }
        return Map.of("error_message", "success", "data", articles
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    @Override
    public Boolean checkLike(Integer articleId, Integer userId) {
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId).eq("user_id", userId);
        ArticleLike articleLike = articleLikeMapper.selectOne(queryWrapper);
        if (articleLike != null) {
            return true;
        } else {
            return false;
        }
    }


}
