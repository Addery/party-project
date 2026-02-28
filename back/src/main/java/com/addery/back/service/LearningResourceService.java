package com.addery.back.service;

import com.addery.back.dto.common.CategoryDTO;
import com.addery.back.dto.learning.LearningResourceDetailDTO;
import com.addery.back.dto.learning.LearningResourceListDTO;
import com.addery.back.dto.learning.RelatedResourceDTO;
import com.addery.back.entity.LearningResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface LearningResourceService {
    
    /**
     * 获取资源分类列表
     * @return 分类列表
     */
    List<CategoryDTO> getCategories();
    
    /**
     * 获取学习资源列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param category 资源类型
     * @param keyword 搜索关键词
     * @return 资源列表和分页信息
     */
    LearningResourceListDTO getResourceList(Integer page, Integer pageSize, String category, String keyword);
    
    /**
     * 获取学习资源详情
     * @param id 资源ID
     * @return 资源详情
     */
    LearningResourceDetailDTO getResourceDetail(Integer id);
    
    /**
     * 记录用户开始学习某个资源的行为
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 学习记录信息
     */
    Object startLearning(Integer resourceId, Integer userId);
    
    /**
     * 获取相关资源
     * @param resourceId 资源ID
     * @param limit 返回数量，默认3
     * @return 相关资源列表
     */
    List<RelatedResourceDTO> getRelatedResources(Integer resourceId, Integer limit);
    
    /**
     * 检查用户是否收藏了指定资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 是否已收藏
     */
    boolean checkFavoriteStatus(Integer resourceId, Integer userId);
    
    /**
     * 用户收藏指定资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     */
    void addFavorite(Integer resourceId, Integer userId);
    
    /**
     * 用户取消收藏指定资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     */
    void removeFavorite(Integer resourceId, Integer userId);
    
    /**
     * 获取首页推荐学习资源列表
     * @param limit 返回数量
     * @return 学习资源列表
     */
    List<LearningResource> getHomeRecommendResources(Integer limit);
    
    /**
     * 用户点赞或取消点赞学习资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 点赞结果（是否点赞成功）
     */
    boolean toggleLike(Integer resourceId, Integer userId);
    
    /**
     * 添加评论
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @param content 评论内容
     * @param parentId 父评论ID，用于回复功能
     * @return 评论ID
     */
    Integer addComment(Integer resourceId, Integer userId, String content, Integer parentId);
    
    /**
     * 获取评论列表
     * @param resourceId 资源ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 评论列表和分页信息
     */
    Object getComments(Integer resourceId, Integer page, Integer pageSize);
    
    /**
     * 记录分享行为
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @param sharePlatform 分享平台
     * @return 分享结果
     */
    boolean shareResource(Integer resourceId, Integer userId, String sharePlatform);
    
    /**
     * 根据分类获取资源列表
     * @param category 分类名称
     * @param pageable 分页参数
     * @return 分页资源列表
     */
    Page<LearningResource> getResourcesByCategory(String category, Pageable pageable);
    
    /**
     * 获取所有资源列表
     * @param pageable 分页参数
     * @return 分页资源列表
     */
    Page<LearningResource> getAllResources(Pageable pageable);
    
    /**
     * 根据ID获取资源
     * @param id 资源ID
     * @return 资源Optional对象
     */
    Optional<LearningResource> getResourceById(Integer id);
    
    /**
     * 增加资源浏览次数
     * @param id 资源ID
     */
    void incrementViews(Integer id);
}
