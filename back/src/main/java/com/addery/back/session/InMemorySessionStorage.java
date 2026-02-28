package com.addery.back.session;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 内存会话存储实现
 */
@Component
public class InMemorySessionStorage implements SessionStorage {
    // 会话存储
    private final ConcurrentHashMap<String, SessionData> sessions = new ConcurrentHashMap<>();
    // 日期会话统计
    private final ConcurrentHashMap<LocalDate, ConcurrentHashMap<String, SessionData>> dateSessions = new ConcurrentHashMap<>();
    // 会话超时时间（默认30分钟）
    private static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);
    // 清理线程池
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    public InMemorySessionStorage() {
        // 每1分钟清理一次超时会话
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredSessions, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public String createSession(SessionData sessionData) {
        // 生成唯一会话ID
        String sessionId = UUID.randomUUID().toString();
        sessionData.setSessionId(sessionId);
        sessionData.setStartTime(LocalDateTime.now());
        sessionData.setLastAccessTime(LocalDateTime.now());
        sessionData.setRequestCount(1);

        // 存储会话
        sessions.put(sessionId, sessionData);

        // 添加到日期统计
        LocalDate today = LocalDate.now();
        dateSessions.computeIfAbsent(today, k -> new ConcurrentHashMap<>())
                .put(sessionId, sessionData);

        return sessionId;
    }

    @Override
    public SessionData getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void updateSessionAccessTime(String sessionId) {
        SessionData sessionData = sessions.get(sessionId);
        if (sessionData != null) {
            sessionData.setLastAccessTime(LocalDateTime.now());
            sessionData.setRequestCount(sessionData.getRequestCount() + 1);
        }
    }

    @Override
    public void closeSession(String sessionId) {
        sessions.remove(sessionId);
        // 从日期统计中移除
        for (Map.Entry<LocalDate, ConcurrentHashMap<String, SessionData>> entry : dateSessions.entrySet()) {
            entry.getValue().remove(sessionId);
        }
    }

    @Override
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<String> expiredSessionIds = new ArrayList<>();

        // 查找超时会话
        for (Map.Entry<String, SessionData> entry : sessions.entrySet()) {
            SessionData session = entry.getValue();
            if (Duration.between(session.getLastAccessTime(), now).compareTo(SESSION_TIMEOUT) > 0) {
                expiredSessionIds.add(entry.getKey());
            }
        }

        // 清理超时会话
        for (String sessionId : expiredSessionIds) {
            closeSession(sessionId);
        }
    }

    @Override
    public long getTotalSessionCount() {
        // 计算所有日期的会话总数
        long total = 0;
        for (Map.Entry<LocalDate, ConcurrentHashMap<String, SessionData>> entry : dateSessions.entrySet()) {
            total += entry.getValue().size();
        }
        return total;
    }

    @Override
    public long getTodaySessionCount() {
        LocalDate today = LocalDate.now();
        return dateSessions.getOrDefault(today, new ConcurrentHashMap<>()).size();
    }

    @Override
    public long getSessionCountByDate(LocalDate date) {
        return dateSessions.getOrDefault(date, new ConcurrentHashMap<>()).size();
    }

    @Override
    public long getActiveSessionCount() {
        LocalDateTime now = LocalDateTime.now();
        long activeCount = 0;

        for (SessionData session : sessions.values()) {
            if (Duration.between(session.getLastAccessTime(), now).compareTo(SESSION_TIMEOUT) <= 0) {
                activeCount++;
            }
        }

        return activeCount;
    }

    @Override
    public long getSessionCountByIp(String ipAddress) {
        long count = 0;
        for (ConcurrentHashMap<String, SessionData> dateMap : dateSessions.values()) {
            for (SessionData session : dateMap.values()) {
                if (ipAddress.equals(session.getIpAddress())) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public List<SessionData> getRecentSessions(int limit) {
        List<SessionData> allSessions = new ArrayList<>();
        for (ConcurrentHashMap<String, SessionData> dateMap : dateSessions.values()) {
            allSessions.addAll(dateMap.values());
        }

        // 按创建时间倒序排序
        return allSessions.stream()
                .sorted(Comparator.comparing(SessionData::getStartTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, Long> getDailySessionStats(int days) {
        Map<LocalDate, Long> stats = new HashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < days; i++) {
            LocalDate date = today.minusDays(i);
            stats.put(date, getSessionCountByDate(date));
        }

        return stats;
    }
}
