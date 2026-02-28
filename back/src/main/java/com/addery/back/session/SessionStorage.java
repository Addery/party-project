package com.addery.back.session;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 会话存储接口
 */
public interface SessionStorage {
    /**
     * 创建新会话
     */
    String createSession(SessionData sessionData);

    /**
     * 获取会话
     */
    SessionData getSession(String sessionId);

    /**
     * 更新会话最后访问时间
     */
    void updateSessionAccessTime(String sessionId);

    /**
     * 关闭会话
     */
    void closeSession(String sessionId);

    /**
     * 清理超时会话
     */
    void cleanupExpiredSessions();

    /**
     * 获取总会话数
     */
    long getTotalSessionCount();

    /**
     * 获取今日会话数
     */
    long getTodaySessionCount();

    /**
     * 获取指定日期的会话数
     */
    long getSessionCountByDate(LocalDate date);

    /**
     * 获取活跃会话数
     */
    long getActiveSessionCount();

    /**
     * 根据IP地址获取会话数
     */
    long getSessionCountByIp(String ipAddress);

    /**
     * 获取最近的会话列表
     */
    List<SessionData> getRecentSessions(int limit);

    /**
     * 获取每日会话统计
     */
    Map<LocalDate, Long> getDailySessionStats(int days);
}
