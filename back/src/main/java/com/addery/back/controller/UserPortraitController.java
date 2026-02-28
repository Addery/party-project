package com.addery.back.controller;

import com.addery.back.dto.common.Response;
import com.addery.back.service.UserPortraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user-portraits")
public class UserPortraitController {
    @Autowired
    private UserPortraitService userPortraitService;

    /**
     * 获取用户画像统计数据
     * @return 用户画像统计数据
     */
    @GetMapping("/stats")
    public Response<Map<String, Object>> getUserPortraitStats() {
        try {
            Map<String, Object> stats = userPortraitService.getUserPortraitStats();
            return Response.success("获取用户画像统计数据成功", stats);
        } catch (Exception e) {
            return Response.error("获取用户画像统计数据失败");
        }
    }

    /**
     * 获取特定用户的画像信息
     * @param userId 用户ID
     * @return 用户画像信息
     */
    @GetMapping("/{userId}")
    public Response<Map<String, Object>> getUserPortrait(@PathVariable Integer userId) {
        try {
            Map<String, Object> portrait = userPortraitService.getUserPortrait(userId);
            return Response.success("获取用户画像信息成功", portrait);
        } catch (Exception e) {
            return Response.error("获取用户画像信息失败");
        }
    }

    /**
     * 获取用户画像趋势分析
     * @param period 时间周期（week/month/year）
     * @return 用户画像趋势分析数据
     */
    @GetMapping("/trends")
    public Response<Map<String, Object>> getUserPortraitTrends(@RequestParam(defaultValue = "month") String period) {
        try {
            Map<String, Object> trends = userPortraitService.getUserPortraitTrends(period);
            return Response.success("获取用户画像趋势分析成功", trends);
        } catch (Exception e) {
            return Response.error("获取用户画像趋势分析失败");
        }
    }
}
