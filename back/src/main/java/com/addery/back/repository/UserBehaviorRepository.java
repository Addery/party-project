package com.addery.back.repository;

import com.addery.back.entity.User;
import com.addery.back.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户行为记录Repository接口
 */
@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Integer> {
    
    /**
     * 根据用户查询行为记录
     * @param user 用户对象
     * @return 用户行为记录列表
     */
    List<UserBehavior> findByUser(User user);
    
    /**
     * 根据用户和内容分类查询行为记录
     * @param user 用户对象
     * @param contentCategory 内容分类
     * @return 用户行为记录列表
     */
    List<UserBehavior> findByUserAndContentCategory(User user, String contentCategory);
    
    /**
     * 根据用户和行为类型查询行为记录
     * @param user 用户对象
     * @param behaviorType 行为类型
     * @return 用户行为记录列表
     */
    List<UserBehavior> findByUserAndBehaviorType(User user, String behaviorType);
    
    /**
     * 根据用户和内容类型查询行为记录
     * @param user 用户对象
     * @param contentType 内容类型
     * @return 用户行为记录列表
     */
    List<UserBehavior> findByUserAndContentType(User user, String contentType);
}
