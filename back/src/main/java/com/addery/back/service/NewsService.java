package com.addery.back.service;

import com.addery.back.dto.RelatedNewsDTO;
import com.addery.back.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    // 获取头条新闻列表
    Page<News> getHeadlineNews(Pageable pageable);
    
    // 根据ID获取新闻详情
    News getNewsById(Integer id);
    
    // 获取新闻列表（支持分类和关键词搜索）
    Page<News> getNewsList(String category, String keyword, Pageable pageable);
    
    // 获取相关新闻
    List<RelatedNewsDTO> getRelatedNews(String category, Integer excludeId, Pageable pageable);
    
    // 获取所有新闻分类
    List<String> getAllCategories();
    
    // 获取首页推荐新闻列表
    List<News> getHomeRecommendNews(Integer limit);
    
    // 更新新闻浏览量
    void updateNewsViewsCount(Integer id);
}
