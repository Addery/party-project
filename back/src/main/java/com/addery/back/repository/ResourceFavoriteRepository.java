package com.addery.back.repository;

import com.addery.back.entity.ResourceFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceFavoriteRepository extends JpaRepository<ResourceFavorite, Integer> {
    
    /**
     * 检查用户是否收藏了指定资源
     * @param userId 用户ID
     * @param resourceId 资源ID
     * @return 是否收藏
     */
    boolean existsByUserIdAndResourceId(Integer userId, Integer resourceId);
    
    /**
     * 根据用户ID和资源ID查找收藏记录
     * @param userId 用户ID
     * @param resourceId 资源ID
     * @return 收藏记录
     */
    Optional<ResourceFavorite> findByUserIdAndResourceId(Integer userId, Integer resourceId);
    
    /**
     * 根据用户ID和资源ID删除收藏记录
     * @param userId 用户ID
     * @param resourceId 资源ID
     */
    void deleteByUserIdAndResourceId(Integer userId, Integer resourceId);
}