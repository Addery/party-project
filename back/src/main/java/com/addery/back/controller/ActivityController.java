package com.addery.back.controller;

import com.addery.back.dto.common.Response;
import com.addery.back.dto.activity.*;
import com.addery.back.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/activities/categories")
    public Response<List<ActivityCategoryDTO>> getActivityCategories() {
        List<ActivityCategoryDTO> categories = activityService.getActivityCategories();
        return Response.success(categories);
    }

    @GetMapping("/activities")
    public Response<ActivityListDTO> getActivityList(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @RequestParam(name = "category", required = false) String category,
                                                    @RequestParam(name = "keyword", required = false) String keyword) {
        ActivityListDTO result = activityService.getActivityList(page, pageSize, category, keyword);
        return Response.success(result);
    }

    @GetMapping("/activities/{id}")
    public Response<ActivityDetailDTO> getActivityDetail(@PathVariable Integer id) {
        ActivityDetailDTO result = activityService.getActivityDetail(id);
        if (result == null) {
            return Response.error("活动不存在");
        }
        return Response.success(result);
    }

    @PostMapping("/activities/{id}/register")
    public Response<ActivityRegistrationResponse> registerActivity(@PathVariable Integer id, @RequestBody ActivityRegistrationRequest request) {
        try {
            ActivityRegistrationResponse result = activityService.registerActivity(id, request);
            return Response.success(result);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/home/activities")
    public Response<List<HomeActivityItemDTO>> getHomeRecommendActivities(@RequestParam(name = "limit", required = false) Integer limit,
                                                                         @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                         @RequestParam(name = "order", required = false) String order) {
        List<HomeActivityItemDTO> result = activityService.getHomeRecommendActivities(limit, sortBy, order);
        return Response.success("获取成功", result);
    }
}
