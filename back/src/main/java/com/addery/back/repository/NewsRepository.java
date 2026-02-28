package com.addery.back.repository;

import com.addery.back.dto.RelatedNewsDTO;
import com.addery.back.entity.News;
import com.addery.back.enums.NewsCategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    // 根据状态获取新闻列表
    Page<News> findByStatus(String status, Pageable pageable);
    
    // 获取置顶新闻
    @Query("SELECT n FROM News n WHERE n.status = ?1 AND n.isTop = true")
    Page<News> findByStatusAndTopTrue(String status, Pageable pageable);
    

    
    // 根据分类获取新闻列表
    Page<News> findByStatusAndCategory(String status, NewsCategoryEnum category, Pageable pageable);
    
    // 根据关键词搜索新闻（标题）
    Page<News> findByStatusAndTitleContaining(String status, String keyword, Pageable pageable);
    
    // 根据分类和关键词搜索新闻
    Page<News> findByStatusAndCategoryAndTitleContaining(String status, NewsCategoryEnum category, String keyword, Pageable pageable);
    
    // 获取相关新闻（同分类，排除当前ID）
    List<News> findByStatusAndCategoryAndIdNot(String status, NewsCategoryEnum category, Integer id, Pageable pageable);
    
    // 获取相关新闻的摘要信息（只返回需要的字段）
    @Query("SELECT new com.addery.back.dto.RelatedNewsDTO(n.id, n.title, n.publishDate, n.category) FROM News n WHERE n.status = ?1 AND n.category = ?2 AND n.id != ?3")
    List<RelatedNewsDTO> findRelatedNewsSummary(String status, NewsCategoryEnum category, Integer id, Pageable pageable);
    
    // 根据状态和排除ID获取新闻列表
    Page<News> findByStatusAndIdNot(String status, Integer id, Pageable pageable);
    
    // 获取所有新闻分类
    @Query("SELECT DISTINCT n.category FROM News n WHERE n.status = 'published' ORDER BY n.category")
    List<NewsCategoryEnum> findAllCategories();
}
