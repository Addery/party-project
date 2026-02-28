package com.addery.back.controller;

import com.addery.back.dto.RelatedNewsDTO;
import com.addery.back.dto.common.Response;
import com.addery.back.entity.News;
import com.addery.back.enums.NewsCategoryEnum;
import com.addery.back.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    /**
     * 获取新闻列表（分页，支持分类和关键词搜索）
     * @param page 当前页码（从1开始）
     * @param pageSize 每页大小
     * @param sortBy 排序字段
     * @param order 排序方向（asc/desc）
     * @param category 新闻分类（可选）
     * @param keyword 搜索关键词（可选）
     * @return 分页新闻列表
     */
    @GetMapping
    public Response<Map<String, Object>> getNewsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "publishDate") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        try {
            // 验证排序字段是否支持
            if (!sortBy.equals("publishDate") && !sortBy.equals("views")) {
                sortBy = "publishDate"; // 默认按发布日期排序
            }
            
            // 如果排序字段是views，则映射到实体类的viewsCount字段
            String actualSortField = sortBy.equals("views") ? "viewsCount" : sortBy;
            
            // 转换为0-based页码
            Pageable pageable = PageRequest.of(
                    page - 1,
                    pageSize,
                    Sort.by(Sort.Direction.fromString(order), actualSortField)
            );
            Page<News> newsPage = newsService.getNewsList(category, keyword, pageable);
            
            // 构建响应数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("newsList", newsPage.getContent());
            responseData.put("total", newsPage.getTotalElements());
            responseData.put("page", page);
            responseData.put("pageSize", pageSize);
            
            return Response.success("获取成功", responseData);
        } catch (Exception e) {
            System.out.println("[ERROR] NewsController.getNewsList 异常: " + e.getMessage());
            e.printStackTrace();
            return Response.error("获取新闻列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取新闻详情
     * @param id 新闻ID
     * @return 新闻详情
     */
    @GetMapping("/{id:\\d+}")
    public Response<News> getNewsById(@PathVariable Integer id) {
        try {
            News news = newsService.getNewsById(id);
            if (news != null) {
                return Response.success("获取新闻详情成功", news);
            } else {
                return Response.error("新闻不存在");
            }
        } catch (Exception e) {
            return Response.error("获取新闻详情失败");
        }
    }

    /**
     * 获取头条新闻（复数形式）
     * @param limit 头条数量限制
     * @return 头条新闻列表
     */
    @GetMapping("/headlines")
    public Response<List<News>> getHeadlineNews(@RequestParam(defaultValue = "3") Integer limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishDate"));
            List<News> headlines = newsService.getHeadlineNews(pageable).getContent();
            return Response.success("获取头条新闻成功", headlines);
        } catch (Exception e) {
            return Response.error("获取头条新闻失败");
        }
    }
    
    /**
     * 获取头条新闻（单数形式，为了兼容API请求）
     * @param limit 头条数量限制
     * @return 头条新闻列表
     */
    @GetMapping("/headline")
    public Response<List<News>> getHeadlineNewsSingle(@RequestParam(defaultValue = "3") Integer limit) {
        // 直接调用复数形式的处理方法
        return getHeadlineNews(limit);
    }
    
    /**
     * 根据当前新闻的分类和ID获取相关新闻列表
     * @param category 当前新闻的分类
     * @param excludeId 当前新闻的ID
     * @return 相关新闻列表
     */
    @GetMapping("/related")
    public Response<List<RelatedNewsDTO>> getRelatedNews(
            @RequestParam String category,
            @RequestParam Integer excludeId) {
        try {
            // 创建分页请求，默认获取3条相关新闻，按发布日期倒序排列
            Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "publishDate"));
            List<RelatedNewsDTO> relatedNews = newsService.getRelatedNews(category, excludeId, pageable);
            return Response.success("获取相关新闻成功", relatedNews);
        } catch (Exception e) {
            return Response.error("获取相关新闻失败");
        }
    }
    
    /**
     * 获取系统中所有新闻分类
     * @return 新闻分类列表
     */
    @GetMapping("/categories")
    public Response<List<String>> getAllCategories() {
        try {
            // 返回新闻分类列表（使用枚举类）
            List<String> categories = Arrays.asList(NewsCategoryEnum.getAllValues());
            return Response.success("成功", categories);
        } catch (Exception e) {
            return Response.error("服务器内部错误");
        }
    }
}
