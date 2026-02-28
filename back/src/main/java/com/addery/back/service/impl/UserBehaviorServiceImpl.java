package com.addery.back.service;

import com.addery.back.entity.User;
import com.addery.back.entity.UserBehavior;
import com.addery.back.repository.UserBehaviorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户行为记录服务类
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {
    @Autowired
    private UserBehaviorRepository userBehaviorRepository;
    
    /**
     * 保存用户行为记录
     * @param userBehavior 用户行为记录
     * @return 保存后的用户行为记录
     */
    public UserBehavior saveUserBehavior(UserBehavior userBehavior) {
        return userBehaviorRepository.save(userBehavior);
    }
    
    /**
     * 批量保存用户行为记录
     * @param userBehaviors 用户行为记录列表
     * @return 保存后的用户行为记录列表
     */
    public List<UserBehavior> saveBatchUserBehaviors(List<UserBehavior> userBehaviors) {
        return userBehaviorRepository.saveAll(userBehaviors);
    }
    
    /**
     * 根据用户获取所有行为记录
     * @param user 用户对象
     * @return 用户行为记录列表
     */
    public List<UserBehavior> getUserBehaviorsByUser(User user) {
        return userBehaviorRepository.findByUser(user);
    }
    
    /**
     * 根据用户统计内容分类的访问次数
     * @param user 用户对象
     * @return 内容分类及其访问次数的映射
     */
    public Map<String, Long> countContentCategoryByUser(User user) {
        List<UserBehavior> behaviors = userBehaviorRepository.findByUser(user);
        return behaviors.stream()
                .collect(Collectors.groupingBy(UserBehavior::getContentCategory, Collectors.counting()));
    }
    
    /**
     * 根据用户和行为类型统计内容分类的访问次数
     * @param user 用户对象
     * @param behaviorType 行为类型
     * @return 内容分类及其访问次数的映射
     */
    public Map<String, Long> countContentCategoryByUserAndBehaviorType(User user, String behaviorType) {
        List<UserBehavior> behaviors = userBehaviorRepository.findByUserAndBehaviorType(user, behaviorType);
        return behaviors.stream()
                .collect(Collectors.groupingBy(UserBehavior::getContentCategory, Collectors.counting()));
    }
    
    /**
     * 根据用户统计内容类型的访问次数
     * @param user 用户对象
     * @return 内容类型及其访问次数的映射
     */
    public Map<String, Long> countContentTypeByUser(User user) {
        List<UserBehavior> behaviors = userBehaviorRepository.findByUser(user);
        return behaviors.stream()
                .collect(Collectors.groupingBy(UserBehavior::getContentType, Collectors.counting()));
    }
}
