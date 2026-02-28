package com.addery.back.service;

import com.addery.back.entity.User;
import com.addery.back.entity.UserBehavior;
import com.addery.back.entity.UserPortrait;
import com.addery.back.repository.UserPortraitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户画像服务类
 * 用于生成和管理用户画像信息
 */
@Service
public class UserPortraitServiceImpl implements UserPortraitService {
    @Autowired
    private UserPortraitRepository userPortraitRepository;
    
    @Autowired
    private UserBehaviorService userBehaviorService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 生成或更新用户画像
     * @param user 用户对象
     * @return 生成的用户画像
     */
    public UserPortrait generateOrUpdateUserPortrait(User user) {
        // 查询是否已存在用户画像
        Optional<UserPortrait> existingPortrait = userPortraitRepository.findByUser(user);
        UserPortrait portrait = existingPortrait.orElse(new UserPortrait());
        
        // 设置用户
        // UserPortrait实体类中没有setUser方法
        
        // 生成党员特质标签，但UserPortrait实体类中没有learningTags字段
        // List<String> learningTags = generateLearningTags(user);
        
        // 生成学习兴趣，但UserPortrait实体类中没有learningInterests字段
        // List<String> learningInterests = generateLearningInterests(user);
        
        // 生成学习偏好分析，但UserPortrait实体类中没有learningPreferences字段
        // Map<String, Object> learningPreferences = generateLearningPreferences(user);
        
        // 保存用户画像
        return userPortraitRepository.save(portrait);
    }
    
    /**
     * 根据用户获取用户画像
     * @param user 用户对象
     * @return 用户画像
     */
    public Optional<UserPortrait> getUserPortraitByUser(User user) {
        return userPortraitRepository.findByUser(user);
    }
    
    /**
     * 根据用户ID获取用户画像
     * @param userId 用户ID
     * @return 用户画像
     */@Override
    public Optional<UserPortrait> getUserPortraitByUserId(Integer userId) {
        return userPortraitRepository.findByUserId(userId);
    }
    
    @Override
    public Map<String, Object> getUserPortraitStats() {
        // 这里可以实现获取用户画像统计数据的逻辑
        // 暂时返回一个空的Map
        return new java.util.HashMap<>();
    }
    
    @Override
    public Map<String, Object> getUserPortrait(Integer userId) {
        // 这里可以实现根据用户ID获取用户画像详细信息的逻辑
        // 暂时返回一个空的Map
        return new java.util.HashMap<>();
    }
    
    @Override
    public Map<String, Object> getUserPortraitTrends(String period) {
        // 这里可以实现获取用户画像趋势分析的逻辑
        // 暂时返回一个空的Map
        return new java.util.HashMap<>();
    }
    
    /**
     * 生成学习标签
     * @param user 用户对象
     * @return 学习标签列表
     */
    private List<String> generateLearningTags(User user) {
        List<String> tags = new ArrayList<>();
        List<UserBehavior> behaviors = userBehaviorService.getUserBehaviorsByUser(user);
        
        // 根据行为数量和类型生成标签
        long totalBehaviors = behaviors.size();
        long readBehaviors = behaviors.stream()
                .filter(b -> "read".equals(b.getBehaviorType()))
                .count();
        long studyBehaviors = behaviors.stream()
                .filter(b -> "study".equals(b.getBehaviorType()))
                .count();
        
        // 根据行为数量添加标签
        if (totalBehaviors > 0) {
            tags.add("资讯关注者");
        }
        if (readBehaviors > 10) {
            tags.add("学习爱好者");
        }
        if (totalBehaviors > 20) {
            tags.add("活跃用户");
        }
        if (behaviors.stream().anyMatch(b -> "时政要闻".equals(b.getContentCategory()))) {
            tags.add("时事敏锐");
        }
        if (studyBehaviors > 5) {
            tags.add("求知若渴");
        }
        
        return tags;
    }
    
    /**
     * 生成学习兴趣
     * @param user 用户对象
     * @return 学习兴趣列表
     */
    private List<String> generateLearningInterests(User user) {
        // 统计各内容分类的访问次数
        Map<String, Long> categoryCounts = userBehaviorService.countContentCategoryByUser(user);
        
        // 按访问次数降序排序
        List<Map.Entry<String, Long>> sortedCategories = new ArrayList<>(categoryCounts.entrySet());
        sortedCategories.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // 取访问次数最多的前5个分类作为学习兴趣
        return sortedCategories.stream()
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 生成学习偏好分析
     * @param user 用户对象
     * @return 学习偏好分析
     */
    private Map<String, Object> generateLearningPreferences(User user) {
        Map<String, Object> preferences = new HashMap<>();
        
        // 统计新闻阅读偏好
        Map<String, Long> newsReading = userBehaviorService.countContentCategoryByUserAndBehaviorType(user, "read");
        // 统计学习主题偏好
        Map<String, Long> learningTopics = userBehaviorService.countContentCategoryByUserAndBehaviorType(user, "study");
        
        preferences.put("newsReading", newsReading);
        preferences.put("learningTopics", learningTopics);
        
        return preferences;
    }
}
