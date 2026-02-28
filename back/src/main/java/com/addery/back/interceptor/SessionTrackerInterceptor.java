package com.addery.back.interceptor;

import com.addery.back.service.SessionTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 会话跟踪拦截器
 * 拦截所有请求，跟踪用户会话
 */
@Component
public class SessionTrackerInterceptor implements HandlerInterceptor {
    
    private final SessionTrackerService sessionTrackerService;
    
    @Autowired
    public SessionTrackerInterceptor(SessionTrackerService sessionTrackerService) {
        this.sessionTrackerService = sessionTrackerService;
    }
    
    /**
     * 请求处理前拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 跟踪会话
        sessionTrackerService.trackSession(request, response);
        // 继续处理请求
        return true;
    }
}
