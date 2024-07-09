package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.ArticleTag;
import com.example.practice.entity.Tag;
import com.example.practice.mapper.ArticleTagMapper;
import com.example.practice.mapper.TagMapper;
import com.example.practice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    // 定义标签数据表操作变量
    @Autowired
    private TagMapper tagMapper;

    // 定义文章标签关系表操作变量
    @Autowired
    private ArticleTagMapper articleTagMapper;

    // 添加标签函数具体逻辑
    @Override
    public Map<String, Object> addTag(String tagName) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag != null) {
            return Map.of("error_message", "标签已存在");
        }
        tagMapper.insert(new Tag(null, tagName));
        return Map.of("error_message", "success");
    }

    // 获取最热门20个标签函数具体逻辑
    @Override
    public Map<String, Object> getTag() {
        QueryWrapper<Tag> queryWrapper1 = new QueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(queryWrapper1);
        if (tags.size() <= 20) {
            List<String> tagNames = tags.stream().map(Tag::getTagName).collect(Collectors.toList());
            return Map.of("error_message", "success", "data", tagNames);
        }
        QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
        List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper);

        // 统计每个tagId出现的次数
        Map<Integer, Integer> cnt = new HashMap<>();
        for (ArticleTag articleTag : articleTags) {
            Integer tagId = articleTag.getTagId();
            cnt.put(tagId, cnt.getOrDefault(tagId, 0) + 1);
        }
        // 使用优先队列（最小堆）来取出出现次数最多的前十个tagId
        PriorityQueue<Map.Entry<Integer, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        // 将所有的entry加入优先队列
        for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
            pq.offer(entry);
            // 如果优先队列中的元素超过20个，移除出现次数最少的那个
            if (pq.size() > 20) {
                pq.poll();
            }
        }
        // 取出前十多的tagId及其出现次数
        List<Map.Entry<Integer, Integer>> top20 = new ArrayList<>();
        while (!pq.isEmpty()) {
            top20.add(pq.poll());
        }
        // 因为优先队列是最小堆，所以需要反转列表顺序，使得出现次数最多的在前面
        Collections.reverse(top20);

        List<String> top20TagName = new ArrayList<>();
        // 返回结果
        for (Map.Entry<Integer, Integer> entry : top20) {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.eq("id", entry.getValue());
            top20TagName.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
        }
        return Map.of("error_message", "success", "data", top20TagName);
    }
}
