package com.addery.back.repository;

import com.addery.back.entity.ResourceComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceCommentRepository extends JpaRepository<ResourceComment, Integer> {
    /**
     * 根据资源ID获取评论列表，按评论时间倒序排列
     * @param resourceId 资源ID
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<ResourceComment> findByResourceIdOrderByCommentTimeDesc(Integer resourceId, Pageable pageable);
    
    /**
     * 根据资源ID统计评论数量
     * @param resourceId 资源ID
     * @return 评论数量
     */
    long countByResourceId(Integer resourceId);
    
    /**
     * 根据父评论ID获取子评论列表
     * @param parentId 父评论ID
     * @param pageable 分页参数
     * @return 子评论列表
     */
    Page<ResourceComment> findByParentIdOrderByCommentTimeAsc(Integer parentId, Pageable pageable);
}
