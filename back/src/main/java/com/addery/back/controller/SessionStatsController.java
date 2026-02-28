package com.addery.back.controller;

import com.addery.back.service.SessionTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话统计控制器
 * 提供API接口获取访问统计信息
 */
@RestController
@RequestMapping("/api/session-stats")
public class SessionStatsController {
    
    private final SessionTrackerService sessionTrackerService;
    
    @Autowired
    public SessionStatsController(SessionTrackerService sessionTrackerService) {
        this.sessionTrackerService = sessionTrackerService;
    }
    
    /**
     * 获取基本会话统计信息
     */
    @GetMapping("/basic")
    public Map<String, Object> getBasicStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", sessionTrackerService.getTotalSessionCount());
        stats.put("todaySessions", sessionTrackerService.getTodaySessionCount());
        stats.put("activeSessions", sessionTrackerService.getActiveSessionCount());
        return stats;
    }
    
    /**
     * 获取总会话数
     */
    @GetMapping("/total")
    public Map<String, Object> getTotalSessions() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalSessions", sessionTrackerService.getTotalSessionCount());
        return result;
    }
    
    /**
     * 获取今日会话数
     */
    @GetMapping("/today")
    public Map<String, Object> getTodaySessions() {
        Map<String, Object> result = new HashMap<>();
        result.put("todaySessions", sessionTrackerService.getTodaySessionCount());
        return result;
    }
    
    /**
     * 获取指定日期的会话数
     */
    @GetMapping("/date/{date}")
    public Map<String, Object> getSessionsByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Map<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("sessionCount", sessionTrackerService.getSessionCountByDate(date));
        return result;
    }
    
    /**
     * 获取活跃会话数
     */
    @GetMapping("/active")
    public Map<String, Object> getActiveSessions() {
        Map<String, Object> result = new HashMap<>();
        result.put("activeSessions", sessionTrackerService.getActiveSessionCount());
        return result;
    }
    
    /**
     * 根据IP地址获取会话数
     */
    @GetMapping("/ip/{ip}")
    public Map<String, Object> getSessionsByIp(@PathVariable("ip") String ipAddress) {
        Map<String, Object> result = new HashMap<>();
        result.put("ipAddress", ipAddress);
        result.put("sessionCount", sessionTrackerService.getSessionCountByIp(ipAddress));
        return result;
    }
    
    /**
     * 获取最近的会话列表
     */
    @GetMapping("/recent")
    public Map<String, Object> getRecentSessions(@RequestParam(defaultValue = "100") int limit) {
        Map<String, Object> result = new HashMap<>();
        result.put("recentSessions", sessionTrackerService.getRecentSessions(limit));
        return result;
    }
    
    /**
     * 获取每日会话统计
     */
    @GetMapping("/daily")
    public Map<String, Object> getDailyStats(@RequestParam(defaultValue = "7") int days) {
        Map<String, Object> result = new HashMap<>();
        result.put("dailyStats", sessionTrackerService.getDailySessionStats(days));
        return result;
    }
    
    /**
     * 手动清理过期会话
     */
    @PostMapping("/cleanup")
    public Map<String, Object> cleanupExpiredSessions() {
        sessionTrackerService.cleanupExpiredSessions();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Expired sessions cleaned up");
        return result;
    }
}
