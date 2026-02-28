package com.addery.back.service;

import com.addery.back.entity.User;
import com.addery.back.entity.UserPortrait;

import java.util.Map;
import java.util.Optional;

public interface UserPortraitService {
    // 生成或更新用户画像
    UserPortrait generateOrUpdateUserPortrait(User user);
    
    // 根据用户获取用户画像
    Optional<UserPortrait> getUserPortraitByUser(User user);
    
    // 根据用户ID获取用户画像
    Optional<UserPortrait> getUserPortraitByUserId(Integer userId);
    
    // 获取用户画像统计数据
    Map<String, Object> getUserPortraitStats();
    
    // 根据用户ID获取用户画像详细信息
    Map<String, Object> getUserPortrait(Integer userId);
    
    // 获取用户画像趋势分析
    Map<String, Object> getUserPortraitTrends(String period);
}