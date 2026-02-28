package com.addery.back.repository;

import com.addery.back.entity.LearningResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Integer> {
    
    // 根据分类获取学习资源列表
    Page<LearningResource> findByCategory(String category, Pageable pageable);
    
    // 根据类型获取学习资源列表
    Page<LearningResource> findByType(String type, Pageable pageable);
    
    // 根据关键词搜索学习资源（标题和描述）
    Page<LearningResource> findByTitleContainingOrDescriptionContaining(String titleKeyword, String descriptionKeyword, Pageable pageable);
    
    // 根据类型和关键词搜索学习资源
    Page<LearningResource> findByTypeAndTitleContainingOrTypeAndDescriptionContaining(
            String type, String titleKeyword, String type2, String descriptionKeyword, Pageable pageable);
    
    // 根据分类和关键词搜索学习资源
    Page<LearningResource> findByCategoryAndTitleContainingOrCategoryAndDescriptionContaining(
            String category, String titleKeyword, String category2, String descriptionKeyword, Pageable pageable);
    
    // 根据关键词搜索学习资源（标题和描述）
    @Query("SELECT lr FROM LearningResource lr WHERE lr.title LIKE %:keyword% OR lr.description LIKE %:keyword%")
    Page<LearningResource> findByKeyword(String keyword, Pageable pageable);
    
    // 根据类型和关键词搜索学习资源
    @Query("SELECT lr FROM LearningResource lr WHERE lr.type = :type AND (lr.title LIKE %:keyword% OR lr.description LIKE %:keyword%)")
    Page<LearningResource> findByTypeAndKeyword(String type, String keyword, Pageable pageable);
    
    // 获取所有学习资源并按上传日期降序排序
    Page<LearningResource> findAllByOrderByUploadDateDesc(Pageable pageable);
}
