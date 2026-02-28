package com.addery.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话实体类
 */
@Entity
@Table(name = "session_tracker")
@Data
public class Session {

    @Id
    @Column(name = "session_id", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "last_access_time", nullable = false)
    private LocalDateTime lastAccessTime;

    @Column(name = "request_count", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int requestCount;

    @Column(name = "client_type", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'Unknown'")
    private String clientType;

    @Column(name = "is_expired", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean expired;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
