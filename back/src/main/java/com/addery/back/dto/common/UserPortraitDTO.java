package com.addery.back.dto.common;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户画像DTO
 * 用于转换后端UserPortrait实体，使其符合前端期望的格式
 */
@Data
public class UserPortraitDTO {
    private Integer id;
    private UserInfo user;
    private List<String> personalityLabels; // 党员特质标签（对应后端的learningTags）
    private List<String> preferenceLabels; // 学习兴趣标签（对应后端的learningInterests）
    private Preferences preferences;
    private Date updatedAt;
    private Date createdAt;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
        private Integer id;
        private String username;
        private String name;
        // 可以根据需要添加其他用户信息字段
    }

    /**
     * 偏好信息内部类
     */
    @Data
    public static class Preferences {
        private Map<String, Integer> newsCategories; // 新闻分类偏好（对应后端的learningPreferences.newsReading）
        private Map<String, Integer> learningTopics; // 学习主题偏好（对应后端的learningPreferences.learningTopics）
    }
}