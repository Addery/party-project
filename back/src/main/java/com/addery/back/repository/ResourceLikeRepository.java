package com.addery.back.repository;

import com.addery.back.entity.ResourceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceLikeRepository extends JpaRepository<ResourceLike, Integer> {
    /**
     * 检查用户是否已点赞指定资源
     * @param userId 用户ID
     * @param resourceId 资源ID
     * @return 是否已点赞
     */
    boolean existsByUserIdAndResourceId(Integer userId, Integer resourceId);
    
    /**
     * 根据用户ID和资源ID删除点赞记录
     * @param userId 用户ID
     * @param resourceId 资源ID
     */
    void deleteByUserIdAndResourceId(Integer userId, Integer resourceId);
    
    /**
     * 根据资源ID统计点赞数量
     * @param resourceId 资源ID
     * @return 点赞数量
     */
    long countByResourceId(Integer resourceId);
}