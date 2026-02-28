package com.addery.back.service;

import com.addery.back.dto.activity.*;
import java.util.List;

public interface ActivityService {
    // 获取活动分类列表
    List<ActivityCategoryDTO> getActivityCategories();
    
    // 获取活动列表（支持分页、分类筛选和关键词搜索）
    ActivityListDTO getActivityList(Integer page, Integer pageSize, String category, String keyword);
    
    // 获取活动详情
    ActivityDetailDTO getActivityDetail(Integer id);
    
    // 活动报名
    ActivityRegistrationResponse registerActivity(Integer activityId, ActivityRegistrationRequest request);
    
    // 获取首页推荐活动列表
    List<HomeActivityItemDTO> getHomeRecommendActivities(Integer limit, String sortBy, String order);
}
