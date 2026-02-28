package com.addery.back.repository;

import com.addery.back.entity.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 会话数据访问接口
 */
public interface SessionRepository extends JpaRepository<Session, String> {

    /**
     * 查找未过期的会话
     * @param expired 是否过期
     * @return 未过期的会话列表
     */
    List<Session> findByExpired(boolean expired);

    /**
     * 根据会话ID查找未过期的会话
     * @param sessionId 会话ID
     * @param expired 是否过期
     * @return 未过期的会话
     */
    Optional<Session> findBySessionIdAndExpired(String sessionId, boolean expired);

    /**
     * 查找在指定时间之前最后访问的会话
     * @param lastAccessTime 最后访问时间
     * @return 会话列表
     */
    List<Session> findByLastAccessTimeBefore(LocalDateTime lastAccessTime);

    /**
     * 统计指定日期的会话数量
     * @param startOfDay 当天开始时间
     * @param endOfDay 当天结束时间
     * @return 会话数量
     */
    long countByStartTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * 统计今日会话数量
     * @param startOfToday 今日开始时间
     * @return 今日会话数量
     */
    long countByStartTimeAfter(LocalDateTime startOfToday);

    /**
     * 根据IP地址统计会话数量
     * @param ipAddress IP地址
     * @return 会话数量
     */
    long countByIpAddress(String ipAddress);

    /**
     * 查找最近的会话
     * @param limit 限制数量
     * @return 最近的会话列表
     */
    @Query(value = "SELECT s FROM Session s WHERE s.expired = false ORDER BY s.startTime DESC", nativeQuery = false)
    List<Session> findRecentSessions(@Param("limit") int limit, Pageable pageable);

    /**
     * 更新会话最后访问时间和请求次数
     * @param sessionId 会话ID
     * @param lastAccessTime 最后访问时间
     * @param requestCount 请求次数
     * @return 更新的行数
     */
    @Modifying
    @Query("UPDATE Session s SET s.lastAccessTime = :lastAccessTime, s.requestCount = :requestCount WHERE s.sessionId = :sessionId AND s.expired = false")
    int updateSessionAccessTime(@Param("sessionId") String sessionId, @Param("lastAccessTime") LocalDateTime lastAccessTime, @Param("requestCount") int requestCount);

    /**
     * 更新会话为过期
     * @param sessionId 会话ID
     * @return 更新的行数
     */
    @Modifying
    @Query("UPDATE Session s SET s.expired = true WHERE s.sessionId = :sessionId")
    int updateSessionToExpired(@Param("sessionId") String sessionId);

    /**
     * 更新所有超时会话为过期
     * @param lastAccessTime 超时时间
     * @return 更新的行数
     */
    @Modifying
    @Query("UPDATE Session s SET s.expired = true WHERE s.lastAccessTime < :lastAccessTime AND s.expired = false")
    int updateExpiredSessions(@Param("lastAccessTime") LocalDateTime lastAccessTime);

    /**
     * 获取指定天数的每日会话统计
     * @param startDate 开始日期
     * @return 每日会话统计
     */
    @Query("SELECT DATE(s.startTime) AS date, COUNT(*) AS count FROM Session s WHERE s.startTime >= :startDate GROUP BY DATE(s.startTime)")
    List<Object[]> getDailySessionStats(@Param("startDate") LocalDateTime startDate);
}
