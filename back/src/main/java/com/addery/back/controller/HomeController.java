package com.addery.back.controller;

import com.addery.back.dto.*;
import com.addery.back.dto.activity.HomeActivityItemDTO;
import com.addery.back.entity.News;
import com.addery.back.service.ActivityService;
import com.addery.back.service.LearningResourceService;
import com.addery.back.service.NewsService;
import com.addery.back.dto.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LearningResourceService learningResourceService;

    @Autowired
    private NewsService newsService;

    /**
     * 获取首页推荐内容
     * @param newsLimit 新闻推荐数量
     * @param activityLimit 活动推荐数量
     * @param resourceLimit 学习资源推荐数量
     * @return 首页推荐内容集合
     */
    @GetMapping("/recommended")
    public Response<Map<String, Object>> recommended(
            @RequestParam(defaultValue = "2") Integer newsLimit,
            @RequestParam(defaultValue = "8") Integer activityLimit,
            @RequestParam(defaultValue = "12") Integer resourceLimit) {
        try {
            // 获取推荐新闻
            List<Map<String, Object>> newsList = newsService.getHomeRecommendNews(newsLimit)
                    .stream()
                    .map(news -> {
                        Map<String, Object> newsMap = new HashMap<>();
                        newsMap.put("id", news.getId());
                        newsMap.put("title", news.getTitle());
                        newsMap.put("description", news.getDescription());
                        newsMap.put("publishDate", news.getPublishDate());
                        newsMap.put("coverImage", news.getCoverImage());
                        return newsMap;
                    })
                    .collect(Collectors.toList());

            // 获取推荐活动
            List<HomeActivityItemDTO> activities = activityService.getHomeRecommendActivities(activityLimit, "date", "desc");

            // 获取推荐学习资源
            List<Map<String, Object>> resources = learningResourceService.getHomeRecommendResources(resourceLimit)
                    .stream()
                    .map(resource -> {
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", resource.getId());
                        dto.put("title", resource.getTitle());
                        dto.put("description", resource.getDescription());
                        dto.put("category", resource.getCategory());
                        dto.put("uploadDate", resource.getUploadDate());
                        dto.put("fileUrl", resource.getUrl());
                        dto.put("coverImage", resource.getImage());
                        dto.put("viewsCount", resource.getViews());
                        return dto;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("news", newsList);
            result.put("activities", activities);
            result.put("learningResources", resources);

            return Response.success("获取推荐内容成功", result);
        } catch (Exception e) {
            logger.error("获取推荐内容失败: {}", e.getMessage(), e);
            return Response.error("获取推荐内容失败");
        }
    }

    /**
     * 获取首页头条新闻
     * @param limit 头条新闻数量限制
     * @return 头条新闻列表
     */
    @GetMapping("/headline-news")
    public Response<List<Map<String, Object>>> getHomeHeadlines(@RequestParam(defaultValue = "3") Integer limit) {
        try {
            // 设置分页参数，按发布日期倒序排序
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishDate"));
            List<News> headlines = newsService.getHeadlineNews(pageable).getContent();
            
            // 转换为文档要求的格式
            List<Map<String, Object>> result = headlines.stream().map(news -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", news.getId());
                item.put("title", news.getTitle());
                item.put("date", news.getPublishDate().toString());
                item.put("source", news.getAuthor());
                item.put("imageUrl", news.getCoverImage());
                return item;
            }).collect(Collectors.toList());
            
            return Response.success("获取首页头条新闻成功", result);
        } catch (Exception e) {
            logger.error("获取首页头条新闻失败", e);
            return Response.error("获取首页头条新闻失败");
        }
    }
}
