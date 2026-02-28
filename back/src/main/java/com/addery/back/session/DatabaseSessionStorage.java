package com.addery.back.session;

import com.addery.back.entity.Session;
import com.addery.back.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据库会话存储实现
 */
@Component
@Primary
public class DatabaseSessionStorage implements SessionStorage {
    // 会话存储
    private final SessionRepository sessionRepository;
    // 会话超时时间（默认30分钟）
    private static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);
    // 清理线程池
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public DatabaseSessionStorage(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        // 每1分钟清理一次超时会话
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredSessions, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public String createSession(SessionData sessionData) {
        // 生成唯一会话ID
        String sessionId = UUID.randomUUID().toString();
        
        // 创建数据库实体
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(sessionData.getUserId());
        session.setIpAddress(sessionData.getIpAddress());
        session.setUserAgent(sessionData.getUserAgent());
        session.setStartTime(LocalDateTime.now());
        session.setLastAccessTime(LocalDateTime.now());
        session.setRequestCount(1);
        session.setClientType(sessionData.getClientType());
        session.setExpired(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        
        // 保存到数据库
        sessionRepository.save(session);
        
        return sessionId;
    }

    @Override
    public SessionData getSession(String sessionId) {
        // 查找未过期的会话
        return sessionRepository.findBySessionIdAndExpired(sessionId, false)
                .map(this::convertToSessionData)
                .orElse(null);
    }

    @Override
    public void updateSessionAccessTime(String sessionId) {
        // 查找会话
        sessionRepository.findBySessionIdAndExpired(sessionId, false)
                .ifPresent(session -> {
                    // 更新最后访问时间和请求次数
                    session.setLastAccessTime(LocalDateTime.now());
                    session.setRequestCount(session.getRequestCount() + 1);
                    session.setUpdatedAt(LocalDateTime.now());
                    // 保存到数据库
                    sessionRepository.save(session);
                });
    }

    @Override
    public void closeSession(String sessionId) {
        // 更新会话为过期
        sessionRepository.updateSessionToExpired(sessionId);
    }

    @Override
    @Transactional
    public void cleanupExpiredSessions() {
        // 计算超时时间
        LocalDateTime expirationTime = LocalDateTime.now().minus(SESSION_TIMEOUT);
        
        // 更新所有超时会话为过期
        sessionRepository.updateExpiredSessions(expirationTime);
    }

    @Override
    public long getTotalSessionCount() {
        // 统计所有会话数量
        return sessionRepository.count();
    }

    @Override
    public long getTodaySessionCount() {
        // 获取今日开始时间
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        
        // 统计今日会话数量
        return sessionRepository.countByStartTimeAfter(startOfToday);
    }

    @Override
    public long getSessionCountByDate(LocalDate date) {
        // 获取指定日期的开始和结束时间
        LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
        
        // 统计指定日期的会话数量
        return sessionRepository.countByStartTimeBetween(startOfDay, endOfDay);
    }

    @Override
    public long getActiveSessionCount() {
        // 计算活跃会话的时间阈值
        LocalDateTime activeTimeThreshold = LocalDateTime.now().minus(SESSION_TIMEOUT);
        
        // 统计活跃会话数量
        List<Session> activeSessions = sessionRepository.findByExpired(false);
        return activeSessions.stream()
                .filter(session -> session.getLastAccessTime().isAfter(activeTimeThreshold))
                .count();
    }

    @Override
    public long getSessionCountByIp(String ipAddress) {
        // 根据IP地址统计会话数量
        return sessionRepository.countByIpAddress(ipAddress);
    }

    @Override
    public List<SessionData> getRecentSessions(int limit) {
        // 创建分页请求
        Pageable pageable = PageRequest.of(0, limit);
        
        // 获取最近的会话
        List<Session> recentSessions = sessionRepository.findRecentSessions(limit, pageable);
        
        // 转换为SessionData列表
        return recentSessions.stream()
                .map(this::convertToSessionData)
                .collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, Long> getDailySessionStats(int days) {
        // 计算开始日期
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        
        // 获取统计数据
        List<Object[]> results = sessionRepository.getDailySessionStats(startDateTime);
        
        // 构建日期到数量的映射
        Map<LocalDate, Long> stats = new HashMap<>();
        
        // 初始化所有日期的统计数量为0
        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            stats.put(date, 0L);
        }
        
        // 填充统计数据
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Long count = ((Number) result[1]).longValue();
            stats.put(date, count);
        }
        
        return stats;
    }
    
    /**
     * 将数据库实体转换为SessionData
     */
    private SessionData convertToSessionData(Session session) {
        SessionData sessionData = new SessionData();
        sessionData.setSessionId(session.getSessionId());
        sessionData.setUserId(session.getUserId());
        sessionData.setIpAddress(session.getIpAddress());
        sessionData.setUserAgent(session.getUserAgent());
        sessionData.setStartTime(session.getStartTime());
        sessionData.setLastAccessTime(session.getLastAccessTime());
        sessionData.setRequestCount(session.getRequestCount());
        sessionData.setClientType(session.getClientType());
        return sessionData;
    }
}
