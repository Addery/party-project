package com.addery.back.repository;

import com.addery.back.entity.ResourceShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceShareRepository extends JpaRepository<ResourceShare, Integer> {
    /**
     * 根据资源ID统计分享数量
     * @param resourceId 资源ID
     * @return 分享数量
     */
    long countByResourceId(Integer resourceId);
    
    /**
     * 根据用户ID统计分享数量
     * @param userId 用户ID
     * @return 分享数量
     */
    long countByUserId(Integer userId);
    
    /**
     * 根据资源ID和分享平台统计分享数量
     * @param resourceId 资源ID
     * @param sharePlatform 分享平台
     * @return 分享数量
     */
    long countByResourceIdAndSharePlatform(Integer resourceId, String sharePlatform);
}