package com.addery.back.repository;

import com.addery.back.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    // 根据分类查询活动
    Page<Activity> findByCategory(String category, Pageable pageable);
    
    // 根据状态查询活动
    Page<Activity> findByStatus(String status, Pageable pageable);
    
    // 根据状态和分类查询活动
    Page<Activity> findByStatusAndCategory(String status, String category, Pageable pageable);
    
    // 根据关键词搜索活动（标题或描述）
    @Query("SELECT a FROM Activity a WHERE a.title LIKE %:keyword% OR a.description LIKE %:keyword%")
    Page<Activity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 根据状态和关键词搜索活动
    @Query("SELECT a FROM Activity a WHERE a.status = :status AND (a.title LIKE %:keyword% OR a.description LIKE %:keyword%)")
    Page<Activity> findByStatusAndKeyword(@Param("status") String status, @Param("keyword") String keyword, Pageable pageable);
    
    // 根据分类和关键词查询活动
    @Query("SELECT a FROM Activity a WHERE a.category = :category AND (a.title LIKE %:keyword% OR a.description LIKE %:keyword%)")
    Page<Activity> findByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);
    
    // 根据状态、分类和关键词查询活动
    @Query("SELECT a FROM Activity a WHERE a.status = :status AND a.category = :category AND (a.title LIKE %:keyword% OR a.description LIKE %:keyword%)")
    Page<Activity> findByStatusAndCategoryAndKeyword(@Param("status") String status, @Param("category") String category, @Param("keyword") String keyword, Pageable pageable);
}