package com.addery.back.service.impl;


import com.addery.back.dto.common.CategoryDTO;
import com.addery.back.dto.learning.LearningResourceDetailDTO;
import com.addery.back.dto.learning.LearningResourceItemDTO;
import com.addery.back.dto.learning.LearningResourceListDTO;
import com.addery.back.dto.learning.RelatedResourceDTO;
import com.addery.back.dto.learning.ResourceAttachmentDTO;
import com.addery.back.entity.LearningResource;
import com.addery.back.entity.ResourceAttachment;
import com.addery.back.entity.ResourceComment;
import com.addery.back.entity.ResourceFavorite;
import com.addery.back.entity.ResourceLike;
import com.addery.back.entity.ResourceShare;
import com.addery.back.repository.LearningResourceRepository;
import com.addery.back.repository.ResourceAttachmentRepository;
import com.addery.back.repository.ResourceCommentRepository;
import com.addery.back.repository.ResourceFavoriteRepository;
import com.addery.back.repository.ResourceLikeRepository;
import com.addery.back.repository.ResourceShareRepository;
import com.addery.back.service.LearningResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LearningResourceServiceImpl implements LearningResourceService {

    private final LearningResourceRepository learningResourceRepository;
private final ResourceAttachmentRepository resourceAttachmentRepository;
private final ResourceFavoriteRepository resourceFavoriteRepository;
private final ResourceLikeRepository resourceLikeRepository;
private final ResourceCommentRepository resourceCommentRepository;
private final ResourceShareRepository resourceShareRepository;

private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

// 固定的资源分类列表
private final List<CategoryDTO> categories = new ArrayList<>();

// 初始化分类列表
@Autowired
public LearningResourceServiceImpl(LearningResourceRepository learningResourceRepository,
                                   ResourceAttachmentRepository resourceAttachmentRepository,
                                   ResourceFavoriteRepository resourceFavoriteRepository,
                                   ResourceLikeRepository resourceLikeRepository,
                                   ResourceCommentRepository resourceCommentRepository,
                                   ResourceShareRepository resourceShareRepository) {
    this.learningResourceRepository = learningResourceRepository;
    this.resourceAttachmentRepository = resourceAttachmentRepository;
    this.resourceFavoriteRepository = resourceFavoriteRepository;
    this.resourceLikeRepository = resourceLikeRepository;
    this.resourceCommentRepository = resourceCommentRepository;
    this.resourceShareRepository = resourceShareRepository;
    categories.add(new CategoryDTO("theory", "理论学习"));
    categories.add(new CategoryDTO("history", "党史教育"));
    categories.add(new CategoryDTO("policy", "政策法规"));
    categories.add(new CategoryDTO("model", "先进典型"));
    categories.add(new CategoryDTO("integrity", "廉政教育"));
}

    @Override
    public List<CategoryDTO> getCategories() {
        return categories;
    }



    @Override
    public LearningResourceListDTO getResourceList(Integer page, Integer pageSize, String category, String keyword) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        // 创建分页参数
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<LearningResource> resourcePage;

        // 根据条件查询数据库
        if (category != null && !category.isEmpty() && keyword != null && !keyword.isEmpty()) {
            // 按分类和关键词查询
            resourcePage = learningResourceRepository.findByCategoryAndTitleContainingOrCategoryAndDescriptionContaining(
                    category, keyword, category, keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            // 只按分类查询
            resourcePage = learningResourceRepository.findByCategory(category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 只按关键词查询
            resourcePage = learningResourceRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, pageable);
        } else {
            // 查询所有资源
            resourcePage = learningResourceRepository.findAll(pageable);
        }

        LearningResourceListDTO result = new LearningResourceListDTO();
        result.setTotal(resourcePage.getTotalElements());
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setPages(resourcePage.getTotalPages());

        List<LearningResourceItemDTO> items = new ArrayList<>();
        for (LearningResource resource : resourcePage.getContent()) {
            LearningResourceItemDTO item = convertToItemDTO(resource);
            items.add(item);
        }

        result.setResources(items);
        return result;
    }

    @Override
    public LearningResourceDetailDTO getResourceDetail(Integer id) {
        // 从数据库中查找资源
        LearningResource resource = learningResourceRepository.findById(id).orElse(null);
        
        if (resource == null) {
            return null;
        }

        LearningResourceDetailDTO result = convertToDetailDTO(resource);

        // 处理真实的附件数据
        List<ResourceAttachmentDTO> attachments = new ArrayList<>();
        if (resource.getAttachments() != null && !resource.getAttachments().isEmpty()) {
            for (ResourceAttachment attachment : resource.getAttachments()) {
                ResourceAttachmentDTO attachmentDTO = new ResourceAttachmentDTO();
                attachmentDTO.setName(attachment.getFileName());
                attachmentDTO.setUrl(attachment.getFilePath());
                attachments.add(attachmentDTO);
            }
        }
        
        result.setAttachments(attachments);

        return result;
    }

    @Override
    public Object startLearning(Integer resourceId, Integer userId) {
        // 这里简化处理，实际项目中应该将学习记录保存到数据库
        Map<String, Object> result = new HashMap<>();
        // 模拟生成一个学习记录ID
        result.put("recordId", Math.abs((resourceId + userId + (int)(System.currentTimeMillis() / 1000)) % 1000000));
        return result;
    }
    
    @Override
    public List<RelatedResourceDTO> getRelatedResources(Integer id, Integer limit) {
        if (id == null) {
            return new ArrayList<>();
        }
        
        // 获取当前资源
        LearningResource currentResource = learningResourceRepository.findById(id).orElse(null);
        if (currentResource == null) {
            return new ArrayList<>();
        }
        
        // 设置默认返回数量
        if (limit == null || limit <= 0) {
            limit = 3;
        }
        
        // 基于分类获取相关资源（实际项目中可以结合标签等进行更复杂的推荐）
        Pageable pageable = PageRequest.of(0, limit);
        Page<LearningResource> relatedResourcesPage = learningResourceRepository.findByCategory(currentResource.getCategory(), pageable);
        
        // 转换为DTO列表
        List<RelatedResourceDTO> relatedResources = new ArrayList<>();
        for (LearningResource resource : relatedResourcesPage.getContent()) {
            // 排除当前资源本身
            if (!resource.getId().equals(id)) {
                RelatedResourceDTO dto = new RelatedResourceDTO();
                dto.setId(resource.getId());
                dto.setTitle(resource.getTitle());
                dto.setType(resource.getType());
                dto.setAuthor(resource.getAuthor());
                if (resource.getUploadDate() != null) {
                    dto.setDate(dateFormat.format(resource.getUploadDate()));
                }
                dto.setImage(resource.getImage());
                relatedResources.add(dto);
            }
        }
        
        return relatedResources;
    }
    
    @Override
    public boolean checkFavoriteStatus(Integer resourceId, Integer userId) {
        if (resourceId == null || userId == null) {
            return false;
        }
        return resourceFavoriteRepository.existsByUserIdAndResourceId(userId, resourceId);
    }
    
    @Override
    public void addFavorite(Integer resourceId, Integer userId) {
        if (resourceId == null || userId == null) {
            throw new IllegalArgumentException("Resource ID and user ID cannot be null");
        }
        
        // 检查是否已经收藏
        if (!resourceFavoriteRepository.existsByUserIdAndResourceId(userId, resourceId)) {
            ResourceFavorite favorite = new ResourceFavorite();
            favorite.setUserId(userId);
            // 使用实体关联而不是直接设置ID
            LearningResource resource = learningResourceRepository.findById(resourceId).orElseThrow(() -> new RuntimeException("资源不存在"));
            favorite.setResource(resource);
            favorite.setCreatedAt(new java.util.Date());
            resourceFavoriteRepository.save(favorite);
        }
    }
    
    @Override
    public void removeFavorite(Integer resourceId, Integer userId) {
        if (resourceId == null || userId == null) {
            throw new IllegalArgumentException("Resource ID and user ID cannot be null");
        }
        
        resourceFavoriteRepository.deleteByUserIdAndResourceId(userId, resourceId);
    }
    
    @Override
    public List<LearningResource> getHomeRecommendResources(Integer limit) {
        // 设置默认返回数量
        if (limit == null || limit <= 0) {
            limit = 6;
        }
        
        // 创建分页请求，只获取前limit条记录，按上传日期降序排序
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "uploadDate"));
        
        // 获取所有学习资源，按上传日期降序排序
        Page<LearningResource> resourcePage = learningResourceRepository.findAllByOrderByUploadDateDesc(pageable);
        
        // 返回学习资源列表
        return resourcePage.getContent();
    }
    
    @Override
    public boolean toggleLike(Integer resourceId, Integer userId) {
        if (resourceId == null || userId == null) {
            throw new IllegalArgumentException("Resource ID and user ID cannot be null");
        }
        
        LearningResource resource = learningResourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            return false;
        }
        
        // 检查用户是否已点赞
        boolean isLiked = resourceLikeRepository.existsByUserIdAndResourceId(userId, resourceId);
        Integer currentLikes = resource.getLikes() == null ? 0 : resource.getLikes();
        
        if (isLiked) {
            // 取消点赞
            resourceLikeRepository.deleteByUserIdAndResourceId(userId, resourceId);
            resource.setLikes(currentLikes - 1);
        } else {
            // 添加点赞
            ResourceLike like = new ResourceLike();
            like.setResource(resource);
            like.setUserId(userId);
            like.setLikeTime(new java.util.Date());
            like.setCreatedAt(new java.util.Date());
            like.setUpdatedAt(new java.util.Date());
            resourceLikeRepository.save(like);
            resource.setLikes(currentLikes + 1);
        }
        
        learningResourceRepository.save(resource);
        return !isLiked;
    }
    
    @Override
    public Integer addComment(Integer resourceId, Integer userId, String content, Integer parentId) {
        if (resourceId == null || userId == null || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Resource ID, user ID and content cannot be null or empty");
        }
        
        LearningResource resource = learningResourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            throw new RuntimeException("Resource not found");
        }
        
        ResourceComment comment = new ResourceComment();
        comment.setResource(resource);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setCommentTime(new java.util.Date());
        comment.setCreatedAt(new java.util.Date());
        comment.setUpdatedAt(new java.util.Date());
        
        ResourceComment savedComment = resourceCommentRepository.save(comment);
        return savedComment.getId();
    }
    
    @Override
    public Object getComments(Integer resourceId, Integer page, Integer pageSize) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID cannot be null");
        }
        
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<ResourceComment> commentPage = resourceCommentRepository.findByResourceIdOrderByCommentTimeDesc(resourceId, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", commentPage.getTotalElements());
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("pages", commentPage.getTotalPages());
        result.put("comments", commentPage.getContent());
        
        return result;
    }
    
    @Override
    public boolean shareResource(Integer resourceId, Integer userId, String sharePlatform) {
        if (resourceId == null || userId == null) {
            throw new IllegalArgumentException("Resource ID and user ID cannot be null");
        }
        
        LearningResource resource = learningResourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            return false;
        }
        
        // 记录分享行为
        ResourceShare share = new ResourceShare();
        share.setResource(resource);
        share.setUserId(userId);
        share.setSharePlatform(sharePlatform);
        share.setShareTime(new java.util.Date());
        share.setCreatedAt(new java.util.Date());
        share.setUpdatedAt(new java.util.Date());
        
        resourceShareRepository.save(share);
        
        // 更新资源分享数
        Integer currentShares = resource.getShares() == null ? 0 : resource.getShares();
        resource.setShares(currentShares + 1);
        learningResourceRepository.save(resource);
        
        return true;
    }
    
    @Override
    public Page<LearningResource> getResourcesByCategory(String category, Pageable pageable) {
        return learningResourceRepository.findByCategory(category, pageable);
    }
    
    @Override
    public Page<LearningResource> getAllResources(Pageable pageable) {
        return learningResourceRepository.findAll(pageable);
    }
    
    @Override
    public Optional<LearningResource> getResourceById(Integer id) {
        return learningResourceRepository.findById(id);
    }
    
    @Override
    public void incrementViews(Integer id) {
        LearningResource resource = learningResourceRepository.findById(id).orElse(null);
        if (resource != null) {
            Integer currentViews = resource.getViews() == null ? 0 : resource.getViews();
            resource.setViews(currentViews + 1);
            learningResourceRepository.save(resource);
        }
    }

    // 辅助方法：将实体转换为ItemDTO
    private LearningResourceItemDTO convertToItemDTO(LearningResource resource) {
        LearningResourceItemDTO dto = new LearningResourceItemDTO();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setAuthor(resource.getAuthor());
        if (resource.getUploadDate() != null) {
            dto.setDate(dateFormat.format(resource.getUploadDate()));
        }
        dto.setType(resource.getType());
        dto.setCategory(resource.getCategory());
        dto.setDescription(resource.getDescription());
        dto.setViews(resource.getViews());
        dto.setDuration(resource.getDuration());
        dto.setLevel(resource.getLevel());
        dto.setRating(resource.getRating());
        dto.setImage(resource.getImage());
        
        // 处理tags字段，将字符串转换为String[]
        if (resource.getTags() != null && !resource.getTags().isEmpty()) {
            dto.setTags(resource.getTags().split(","));
        }
        
        return dto;
    }

    // 辅助方法：将实体转换为DetailDTO
    private LearningResourceDetailDTO convertToDetailDTO(LearningResource resource) {
        LearningResourceDetailDTO dto = new LearningResourceDetailDTO();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setAuthor(resource.getAuthor());
        if (resource.getUploadDate() != null) {
            dto.setDate(dateFormat.format(resource.getUploadDate()));
        }
        dto.setType(resource.getType());
        dto.setCategory(resource.getCategory());
        dto.setDescription(resource.getDescription());
        dto.setViews(resource.getViews());
        dto.setUrl(resource.getUrl());
        dto.setDuration(resource.getDuration());
        dto.setLevel(resource.getLevel());
        dto.setRating(resource.getRating());
        dto.setImage(resource.getImage());
        dto.setContent(resource.getContent());
        
        // 处理tags字段，将字符串转换为List<String>
        if (resource.getTags() != null && !resource.getTags().isEmpty()) {
            dto.setTags(List.of(resource.getTags().split(",")));
        }
        
        return dto;
    }
}
