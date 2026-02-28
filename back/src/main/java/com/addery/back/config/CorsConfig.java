package com.addery.back.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname CorsConfig
 * @Description TODO
 * <p>
 * @Date 2025/12/6 12:44
 * @Created by 14121
 * @Author Addery
 * @Version 1.0
 */


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // 允许跨域的路径
                        .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*") // Spring Boot 2.4+ 推荐用 patterns（支持通配符）
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false) // 如果前端带 credentials（如 cookies），设为 true，但 origin 不能为 *
                        .maxAge(3600); // 预检请求缓存时间（秒）
            }
        };
    }
}
