package com.addery.back.service.impl;

import com.addery.back.dto.RelatedNewsDTO;
import com.addery.back.entity.News;
import com.addery.back.enums.NewsCategoryEnum;
import com.addery.back.repository.NewsRepository;
import com.addery.back.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class NewsServiceImpl implements NewsService {
    
    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<News> getHeadlineNews(Pageable pageable) {
        // 获取已发布的新闻，按发布日期倒序排列，取最新的新闻
        return newsRepository.findByStatus("published", pageable);
    }

    @Override
    public News getNewsById(Integer id) {
        // 根据ID获取新闻详情
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public Page<News> getNewsList(String category, String keyword, Pageable pageable) {
        try {
            // 根据分类和关键词筛选新闻
            if (category != null && !category.isEmpty() && keyword != null && !keyword.isEmpty()) {
                // 分类和关键词都有
                System.out.println("[DEBUG] 分类和关键词都有: category=" + category + ", keyword=" + keyword);
                NewsCategoryEnum categoryEnum = NewsCategoryEnum.fromValue(category);
                // 如果通过英文名称没找到，尝试通过中文显示名称查找
                if (categoryEnum == null) {
                    categoryEnum = NewsCategoryEnum.fromDisplayName(category);
                }
                if (categoryEnum != null) {
                    return newsRepository.findByStatusAndCategoryAndTitleContaining("published", categoryEnum, keyword, pageable);
                } else {
                    // 如果分类不存在，返回空结果
                    System.out.println("[DEBUG] 分类不存在: " + category);
                    return Page.empty(pageable);
                }
            } else if (category != null && !category.isEmpty()) {
                // 只有分类
                System.out.println("[DEBUG] 只有分类: " + category);
                NewsCategoryEnum categoryEnum = NewsCategoryEnum.fromValue(category);
                // 如果通过英文名称没找到，尝试通过中文显示名称查找
                if (categoryEnum == null) {
                    categoryEnum = NewsCategoryEnum.fromDisplayName(category);
                }
                if (categoryEnum != null) {
                    return newsRepository.findByStatusAndCategory("published", categoryEnum, pageable);
                } else {
                    // 如果分类不存在，返回空结果
                    System.out.println("[DEBUG] 分类不存在: " + category);
                    return Page.empty(pageable);
                }
            } else if (keyword != null && !keyword.isEmpty()) {
                // 只有关键词
                System.out.println("[DEBUG] 只有关键词: " + keyword);
                return newsRepository.findByStatusAndTitleContaining("published", keyword, pageable);
            } else {
                // 没有筛选条件
                System.out.println("[DEBUG] 没有筛选条件");
                Page<News> result = newsRepository.findByStatus("published", pageable);
                System.out.println("[DEBUG] 查询结果: total=" + result.getTotalElements() + ", content.size()=" + result.getContent().size());
                return result;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] getNewsList 异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<RelatedNewsDTO> getRelatedNews(String category, Integer excludeId, Pageable pageable) {
        // 根据分类筛选相关新闻
        NewsCategoryEnum categoryEnum = NewsCategoryEnum.fromValue(category);
        // 如果通过英文名称没找到，尝试通过中文显示名称查找
        if (categoryEnum == null) {
            categoryEnum = NewsCategoryEnum.fromDisplayName(category);
        }
        if (categoryEnum != null) {
            // 对于有效分类，返回同分类且排除当前ID的相关新闻
            return newsRepository.findRelatedNewsSummary("published", categoryEnum, excludeId, pageable);
        } else {
            // 如果分类不存在，返回空列表
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getAllCategories() {
        // 使用枚举类返回所有分类
        return Arrays.asList(NewsCategoryEnum.getAllValues());
    }
    
    @Override
    public List<News> getHomeRecommendNews(Integer limit) {
        // 设置默认返回数量
        if (limit == null || limit <= 0) {
            limit = 2;
        }
        
        // 创建分页请求，只获取前limit条记录
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishDate"));
        
        // 获取已发布的新闻，按发布日期降序排序
        Page<News> newsPage = newsRepository.findByStatus("published", pageable);
        
        // 打印获取到的新闻数量
        System.out.println("获取到的推荐新闻数量: " + newsPage.getContent().size());
        
        // 返回新闻列表
        return newsPage.getContent();
    }

    @Override
    public void updateNewsViewsCount(Integer id) {
        // 根据ID获取新闻
        News news = newsRepository.findById(id).orElse(null);
        if (news != null) {
            // 浏览量加1
            if (news.getViewsCount() == null) {
                news.setViewsCount(1);
            } else {
                news.setViewsCount(news.getViewsCount() + 1);
            }
            // 保存更新后的新闻
            newsRepository.save(news);
        }
    }
}
