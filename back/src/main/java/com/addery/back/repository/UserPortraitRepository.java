package com.addery.back.repository;

import com.addery.back.entity.User;
import com.addery.back.entity.UserPortrait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户画像Repository接口
 */
@Repository
public interface UserPortraitRepository extends JpaRepository<UserPortrait, Integer> {
    
    /**
     * 根据用户查询用户画像
     * @param user 用户对象
     * @return 用户画像对象，可能为空
     */
    Optional<UserPortrait> findByUser(User user);
    
    /**
     * 根据用户ID查询用户画像
     * @param userId 用户ID
     * @return 用户画像对象，可能为空
     */
    Optional<UserPortrait> findByUserId(Integer userId);
    
    /**
     * 检查用户是否已有画像
     * @param user 用户对象
     * @return 是否已存在
     */
    boolean existsByUser(User user);
}
