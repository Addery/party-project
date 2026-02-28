package com.addery.back.service;

import com.addery.back.session.InMemorySessionStorage;
import com.addery.back.session.SessionData;
import com.addery.back.session.SessionStorage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户会话跟踪服务
 */
@Service
public class SessionTrackerService {
    // 会话存储
    private final SessionStorage sessionStorage;
    // 会话Cookie名称
    private static final String SESSION_COOKIE_NAME = "user_session_id";
    // 会话超时时间（毫秒）
    private static final long SESSION_TIMEOUT_MS = 30 * 60 * 1000; // 30分钟

    @Autowired
    public SessionTrackerService(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    /**
     * 处理用户请求，跟踪会话
     */
    public void trackSession(HttpServletRequest request, HttpServletResponse response) {
        // 清理过期会话
        sessionStorage.cleanupExpiredSessions();

        // 获取会话ID
        String sessionId = getSessionIdFromCookie(request);
        SessionData sessionData = null;

        // 如果有会话ID，检查会话是否有效
        if (sessionId != null) {
            sessionData = sessionStorage.getSession(sessionId);
            // 检查会话是否存在且未超时
            if (sessionData != null) {
                long lastAccessTime = sessionData.getLastAccessTime().toEpochSecond(java.time.ZoneOffset.UTC) * 1000;
                long currentTime = System.currentTimeMillis();
                
                if (currentTime - lastAccessTime <= SESSION_TIMEOUT_MS) {
                    // 更新会话最后访问时间
                    sessionStorage.updateSessionAccessTime(sessionId);
                    return;
                }
                // 会话超时，关闭会话
                sessionStorage.closeSession(sessionId);
                // 删除会话Cookie
                deleteSessionCookie(response);
            }
        }

        // 创建新会话
        sessionData = createSessionData(request);
        sessionId = sessionStorage.createSession(sessionData);
        // 设置会话Cookie
        setSessionCookie(response, sessionId);
    }

    /**
     * 创建会话数据
     */
    private SessionData createSessionData(HttpServletRequest request) {
        SessionData sessionData = new SessionData();
        sessionData.setIpAddress(getClientIp(request));
        sessionData.setUserAgent(request.getHeader("User-Agent"));
        sessionData.setClientType(determineClientType(request.getHeader("User-Agent")));
        // 目前没有用户认证，userId设为空
        sessionData.setUserId(null);
        return sessionData;
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个非unknown的IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    /**
     * 判断客户端类型
     */
    private String determineClientType(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }
        if (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iOS")) {
            return "Mobile";
        } else {
            return "Desktop";
        }
    }

    /**
     * 从Cookie中获取会话ID
     */
    private String getSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置会话Cookie
     */
    private void setSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge((int) (SESSION_TIMEOUT_MS / 1000));
        cookie.setHttpOnly(true);
        cookie.setSecure(requestIsSecure(response));
        response.addCookie(cookie);
    }

    /**
     * 删除会话Cookie
     */
    private void deleteSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(requestIsSecure(response));
        response.addCookie(cookie);
    }

    /**
     * 判断请求是否使用HTTPS
     */
    private boolean requestIsSecure(HttpServletResponse response) {
        // 简单判断，实际生产环境可能需要更复杂的逻辑
        return false;
    }

    /**
     * 获取总会话数
     */
    public long getTotalSessionCount() {
        return sessionStorage.getTotalSessionCount();
    }

    /**
     * 获取今日会话数
     */
    public long getTodaySessionCount() {
        return sessionStorage.getTodaySessionCount();
    }

    /**
     * 获取指定日期的会话数
     */
    public long getSessionCountByDate(LocalDate date) {
        return sessionStorage.getSessionCountByDate(date);
    }

    /**
     * 获取活跃会话数
     */
    public long getActiveSessionCount() {
        return sessionStorage.getActiveSessionCount();
    }

    /**
     * 根据IP地址获取会话数
     */
    public long getSessionCountByIp(String ipAddress) {
        return sessionStorage.getSessionCountByIp(ipAddress);
    }

    /**
     * 获取最近的会话列表
     */
    public List<SessionData> getRecentSessions(int limit) {
        return sessionStorage.getRecentSessions(limit);
    }

    /**
     * 获取每日会话统计
     */
    public Map<LocalDate, Long> getDailySessionStats(int days) {
        return sessionStorage.getDailySessionStats(days);
    }

    /**
     * 手动清理过期会话
     */
    public void cleanupExpiredSessions() {
        sessionStorage.cleanupExpiredSessions();
    }
}
