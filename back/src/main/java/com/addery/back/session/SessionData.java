package com.addery.back.session;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户会话数据模型
 */
@Data
public class SessionData {
    private String sessionId;
    private String userId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime startTime;
    private LocalDateTime lastAccessTime;
    private int requestCount;
    private String clientType;
}
