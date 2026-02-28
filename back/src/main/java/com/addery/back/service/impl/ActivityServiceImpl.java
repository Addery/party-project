package com.addery.back.service.impl;

import com.addery.back.dto.activity.*;
import com.addery.back.dto.common.AttachmentDTO;
import com.addery.back.entity.Activity;
import com.addery.back.entity.ActivityAttachment;
import com.addery.back.entity.ActivityRegistration;
import com.addery.back.repository.ActivityRepository;
import com.addery.back.repository.ActivityRegistrationRepository;
import com.addery.back.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityRegistrationRepository activityRegistrationRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<ActivityCategoryDTO> getActivityCategories() {
        List<ActivityCategoryDTO> categories = new ArrayList<>();
        
        ActivityCategoryDTO category1 = new ActivityCategoryDTO();
        category1.setId("theme");
        category1.setName("主题党日");
        categories.add(category1);
        
        ActivityCategoryDTO category2 = new ActivityCategoryDTO();
        category2.setId("study");
        category2.setName("学习教育");
        categories.add(category2);
        
        ActivityCategoryDTO category3 = new ActivityCategoryDTO();
        category3.setId("volunteer");
        category3.setName("志愿服务");
        categories.add(category3);
        
        ActivityCategoryDTO category4 = new ActivityCategoryDTO();
        category4.setId("democratic");
        category4.setName("民主生活");
        categories.add(category4);
        
        ActivityCategoryDTO category5 = new ActivityCategoryDTO();
        category5.setId("social");
        category5.setName("社会实践");
        categories.add(category5);
        
        ActivityCategoryDTO category6 = new ActivityCategoryDTO();
        category6.setId("training");
        category6.setName("培训交流");
        categories.add(category6);
        
        return categories;
    }

    @Override
    public ActivityListDTO getActivityList(Integer page, Integer pageSize, String category, String keyword) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Activity> activityPage;
        String status = "published"; // 只查询已发布的活动

        if (category != null && !category.isEmpty() && keyword != null && !keyword.isEmpty()) {
            activityPage = activityRepository.findByStatusAndCategoryAndKeyword(status, category, keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            activityPage = activityRepository.findByStatusAndCategory(status, category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            activityPage = activityRepository.findByStatusAndKeyword(status, keyword, pageable);
        } else {
            activityPage = activityRepository.findByStatus(status, pageable);
        }

        ActivityListDTO result = new ActivityListDTO();
        result.setTotal(activityPage.getTotalElements());
        result.setPages(activityPage.getTotalPages());
        result.setPage(page);
        result.setPageSize(pageSize);

        List<ActivityItemDTO> items = new ArrayList<>();
        for (Activity activity : activityPage.getContent()) {
            ActivityItemDTO item = new ActivityItemDTO();
            item.setId(activity.getId());
            item.setTitle(activity.getTitle());
            if (activity.getStartTime() != null) {
                LocalDate date = activity.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                item.setDate(date.format(dateFormatter));
            }
            // item.setTime(activity.getTime()); // Activity类没有此方法
            item.setLocation(activity.getLocation());
            item.setCategory(activity.getCategory());
            item.setStatus(activity.getStatus());
            item.setDescription(activity.getDescription());
            // item.setCanRegister(activity.getCanRegister()); // Activity类没有此方法
            item.setParticipants(activity.getCurrentParticipants());
            // item.setViews(activity.getViews()); // Activity类没有此方法
            item.setImage(activity.getCoverImage());
            items.add(item);
        }

        result.setActivities(items);
        return result;
    }

    @Override
    public ActivityDetailDTO getActivityDetail(Integer id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        if (optionalActivity.isEmpty()) {
            return null;
        }

        Activity activity = optionalActivity.get();
        ActivityDetailDTO result = new ActivityDetailDTO();
        result.setId(activity.getId());
        result.setTitle(activity.getTitle());
        if (activity.getStartTime() != null) {
            LocalDate date = activity.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            result.setDate(date.format(dateFormatter));
        }
        // result.setTime(activity.getTime()); // Activity类没有此方法，使用startTime代替
        result.setLocation(activity.getLocation());
        result.setCategory(activity.getCategory());
        result.setStatus(activity.getStatus());
        result.setDescription(activity.getDescription());
        result.setContent(activity.getContent());
        // result.setCanRegister(activity.getCanRegister()); // Activity类没有此方法
        // registrationDeadline字段已被注释掉
        // if (activity.getRegistrationDeadline() != null) {
        //     LocalDateTime deadline = activity.getRegistrationDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //     result.setRegisterDeadline(deadline.format(dateFormatter));
        // }
        result.setParticipants(activity.getCurrentParticipants());
        result.setCapacity(activity.getMaxParticipants());
        // result.setViews(activity.getViews()); // Activity类没有此方法
        result.setImage(activity.getCoverImage());

        // 处理附件
        List<AttachmentDTO> attachments = new ArrayList<>();
        if (activity.getAttachments() != null) {
            for (ActivityAttachment attachment : activity.getAttachments()) {
                AttachmentDTO dto = new AttachmentDTO();
                dto.setName(attachment.getFileName());
                dto.setUrl(attachment.getFilePath());
                attachments.add(dto);
            }
        }
        result.setAttachments(attachments);

        return result;
    }

    @Override
    public ActivityRegistrationResponse registerActivity(Integer activityId, ActivityRegistrationRequest request) {
        // 验证活动是否存在
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if (optionalActivity.isEmpty()) {
            throw new RuntimeException("活动不存在");
        }

        Activity activity = optionalActivity.get();

        // 验证用户是否已报名
        Optional<ActivityRegistration> existingRegistration = activityRegistrationRepository.findByActivityIdAndUserId(activityId, request.getUserId());
        if (existingRegistration.isPresent()) {
            throw new RuntimeException("已报名参加该活动");
        }

        // registrationDeadline字段已被注释掉，暂不验证报名截止时间
        // if (activity.getRegistrationDeadline() != null) {
        //     LocalDateTime deadline = activity.getRegistrationDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //     if (LocalDateTime.now().isAfter(deadline)) {
        //         throw new RuntimeException("活动报名已截止");
        //     }
        // }

        // 验证活动人数是否已满
        if (activity.getCurrentParticipants() != null && activity.getMaxParticipants() != null && activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动人数已满");
        }

        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setActivity(activity);
        registration.setUserId(request.getUserId());
        registration.setRegistrationTime(new Date());
        registration.setStatus("registered");
        
        ActivityRegistration savedRegistration = activityRegistrationRepository.save(registration);

        // 更新活动参与人数
        if (activity.getCurrentParticipants() == null) {
            activity.setCurrentParticipants(1);
        } else {
            activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        }
        activityRepository.save(activity);

        // 构建响应
        ActivityRegistrationResponse response = new ActivityRegistrationResponse();
        response.setActivityId(savedRegistration.getActivity().getId());
        response.setUserId(savedRegistration.getUserId());
        LocalDateTime registerTime = savedRegistration.getRegistrationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        response.setRegisterTime(registerTime);
        response.setStatus(savedRegistration.getStatus());

        return response;
    }

    @Override
    public List<HomeActivityItemDTO> getHomeRecommendActivities(Integer limit, String sortBy, String order) {
        // 设置默认参数
        if (limit == null || limit < 1 || limit > 20) {
            limit = 6;
        }
        if (sortBy == null || (!sortBy.equals("date") && !sortBy.equals("participants") && !sortBy.equals("views"))) {
            sortBy = "date";
        }
        if (order == null || (!order.equals("asc") && !order.equals("desc"))) {
            order = "desc";
        }

        // 映射排序字段到实体字段
        String sortField = "startTime"; // 默认按活动开始时间排序
        if (sortBy.equals("participants")) {
            sortField = "currentParticipants";
        } else if (sortBy.equals("views")) {
            // Activity实体类中没有views字段，所以使用startTime替代
            sortField = "startTime";
        }
        
        // 获取已发布的活动列表，根据参数排序
        List<Activity> activities = activityRepository.findAll(
            org.springframework.data.domain.Sort.by(
                order.equals("asc") ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC, 
                sortField
            )
        ).stream()
        .filter(activity -> "published".equals(activity.getStatus())) // 只保留已发布的活动
        .limit(limit) // 限制数量
        .toList();

        // 转换为HomeActivityItemDTO
        List<HomeActivityItemDTO> result = new ArrayList<>();
        for (Activity activity : activities) {
            HomeActivityItemDTO item = new HomeActivityItemDTO();
            item.setId(activity.getId());
            item.setTitle(activity.getTitle());
            item.setType("活动"); // 固定为"活动"
            if (activity.getStartTime() != null) {
                LocalDate date = activity.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                item.setDate(date.format(dateFormatter));
            }
            item.setLocation(activity.getLocation());
            item.setParticipants(activity.getCurrentParticipants());
            // item.setViews(activity.getViews()); // Activity实体类中没有views字段
            item.setStatus(activity.getStatus());
            item.setImage(activity.getCoverImage());
            
            // 根据活动分类设置标签
            List<String> tags = new ArrayList<>();
            String category = activity.getCategory();
            if (category != null) {
                switch (category) {
                    case "theme":
                        tags.add("主题党日");
                        break;
                    case "study":
                        tags.add("学习教育");
                        break;
                    case "volunteer":
                        tags.add("志愿服务");
                        break;
                    case "democratic":
                        tags.add("民主生活");
                        break;
                    case "social":
                        tags.add("社会实践");
                        break;
                    case "training":
                        tags.add("培训交流");
                        break;
                    default:
                        tags.add("其他活动");
                }
            }
            item.setTags(tags);
            
            result.add(item);
        }

        return result;
    }
}
