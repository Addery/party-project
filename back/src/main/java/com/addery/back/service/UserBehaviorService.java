package com.addery.back.service;

import com.addery.back.entity.User;
import com.addery.back.entity.UserBehavior;

import java.util.List;
import java.util.Map;

public interface UserBehaviorService {
    // 保存用户行为记录
    UserBehavior saveUserBehavior(UserBehavior userBehavior);
    
    // 批量保存用户行为记录
    List<UserBehavior> saveBatchUserBehaviors(List<UserBehavior> userBehaviors);
    
    // 根据用户获取行为记录
    List<UserBehavior> getUserBehaviorsByUser(User user);
    
    // 统计用户内容分类
    Map<String, Long> countContentCategoryByUser(User user);
    
    // 统计用户指定行为类型的内容分类
    Map<String, Long> countContentCategoryByUserAndBehaviorType(User user, String behaviorType);
    
    // 统计用户内容类型
    Map<String, Long> countContentTypeByUser(User user);
}