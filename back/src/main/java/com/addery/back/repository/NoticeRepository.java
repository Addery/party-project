package com.addery.back.repository;

import com.addery.back.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    // 获取最新的通知公告，用于首页展示
    Page<Notice> findAllByOrderByPublishDateDesc(Pageable pageable);

    // 根据分类和关键词搜索通知公告
    @Query("select n from Notice n where (n.category = :category or :category is null) and (n.title like %:keyword% or n.content like %:keyword%)")
    Page<Notice> findByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);

    // 根据关键词搜索通知公告（标题和内容）
    @Query("select n from Notice n where n.title like %:keyword% or n.content like %:keyword%")
    Page<Notice> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 获取所有通知公告分类
    @Query("select distinct n.category from Notice n where n.category is not null and n.category != ''")
    List<String> findAllCategories();

    @Modifying
    @Query("update Notice n set n.views = n.views + 1 where n.id = :id")
    void incrementViews(Integer id);
}