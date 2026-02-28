package com.addery.back.controller;

import com.addery.back.dto.common.CategoryDTO;
import com.addery.back.dto.common.Response;
import com.addery.back.entity.LearningResource;
import com.addery.back.service.LearningResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/learning")
public class LearningResourceController {
    @Autowired
    private LearningResourceService learningResourceService;

    /**
     * 获取资源分类列表
     * @return 分类列表
     */
    @GetMapping("/categories")
    public Response<List<CategoryDTO>> getCategories() {
        try {
            List<CategoryDTO> categories = learningResourceService.getCategories();
            return Response.success("获取资源分类列表成功", categories);
        } catch (Exception e) {
            return Response.error("获取资源分类列表失败");
        }
    }

    /**
     * 获取学习资源列表（分页）
     * @param page 当前页码（从1开始）
     * @param size 每页大小
     * @param category 分类筛选
     * @param sortField 排序字段
     * @param sortDirection 排序方向（asc/desc）
     * @return 分页学习资源列表
     */
    @GetMapping("/resources")
    public Response<Page<LearningResource>> getLearningResourceList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "uploadDate") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // 转换为0-based页码
            Pageable pageable = PageRequest.of(
                    page - 1,
                    size,
                    Sort.by(Sort.Direction.fromString(sortDirection), sortField)
            );
            Page<LearningResource> resourcePage;
            if (category != null && !category.isEmpty()) {
                resourcePage = learningResourceService.getResourcesByCategory(category, pageable);
            } else {
                resourcePage = learningResourceService.getAllResources(pageable);
            }
            return Response.success("获取学习资源列表成功", resourcePage);
        } catch (Exception e) {
            return Response.error("获取学习资源列表失败");
        }
    }

    /**
     * 根据ID获取学习资源详情
     * @param id 学习资源ID
     * @return 学习资源详情
     */
    @GetMapping("/{id}")
    public Response<LearningResource> getLearningResourceById(@PathVariable Integer id) {
        try {
            Optional<LearningResource> resource = learningResourceService.getResourceById(id);
            if (resource.isPresent()) {
                // 增加浏览次数
                learningResourceService.incrementViews(id);
                return Response.success("获取学习资源详情成功", resource.get());
            } else {
                return Response.error("学习资源不存在");
            }
        } catch (Exception e) {
            return Response.error("获取学习资源详情失败");
        }
    }

    /**
     * 获取热门学习资源
     * @param limit 数量限制
     * @return 热门学习资源列表
     */
    @GetMapping("/resources/popular")
    public Response<List<LearningResource>> getPopularResources(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "views"));
            List<LearningResource> popularResources = learningResourceService.getAllResources(pageable).getContent();
            return Response.success("获取热门学习资源成功", popularResources);
        } catch (Exception e) {
            return Response.error("获取热门学习资源失败");
        }
    }

    /**
     * 记录用户开始学习某个资源的行为
     * @param resourceId 资源ID
     * @return 学习记录信息
     */
    @PostMapping("/resources/{resourceId}/start")
    public Response<Object> startLearning(@PathVariable Integer resourceId) {
        try {
            // 模拟用户ID为1（实际应该从JWT中获取）
            Integer userId = 1;
            Object result = learningResourceService.startLearning(resourceId, userId);
            return Response.success("开始学习成功", result);
        } catch (Exception e) {
            return Response.error("开始学习失败");
        }
    }
    
    /**
     * 获取相关学习资源
     * @param id 学习资源ID
     * @param limit 返回数量，默认3
     * @return 相关学习资源列表
     */
    @GetMapping("/resources/{id}/related")
    public Response<List<com.addery.back.dto.learning.RelatedResourceDTO>> getRelatedResources(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "3") Integer limit) {
        try {
            List<com.addery.back.dto.learning.RelatedResourceDTO> relatedResources = learningResourceService.getRelatedResources(id, limit);
            return Response.success("获取相关学习资源成功", relatedResources);
        } catch (Exception e) {
            return Response.error("获取相关学习资源失败");
        }
    }
}
