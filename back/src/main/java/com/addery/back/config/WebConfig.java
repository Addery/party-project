package com.addery.back.config;

import com.addery.back.interceptor.SessionTrackerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置拦截器等Web相关组件
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final SessionTrackerInterceptor sessionTrackerInterceptor;
    
    @Autowired
    public WebConfig(SessionTrackerInterceptor sessionTrackerInterceptor) {
        this.sessionTrackerInterceptor = sessionTrackerInterceptor;
    }
    
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册会话跟踪拦截器，仅拦截首页接口请求
        registry.addInterceptor(sessionTrackerInterceptor)
                .addPathPatterns("/api/home/**");
    }
}
