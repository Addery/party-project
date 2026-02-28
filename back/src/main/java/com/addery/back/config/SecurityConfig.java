package com.addery.back.config;

import com.addery.back.config.JwtAuthenticationFilter;
import com.addery.back.service.UserDetailsServiceImpl;
import com.addery.back.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护
                .csrf(csrf -> csrf.disable())
                // 允许跨域请求
                .cors(cors -> cors.disable())
                // 设置会话管理策略为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置权限控制
                .authorizeHttpRequests(authz -> authz
                        // 允许所有用户访问静态资源
                        .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.jpeg", "/**/*.svg", "/**/*.css", "/**/*.js").permitAll()
                        // 允许所有用户访问的路径
                        .requestMatchers("/api/auth/**").permitAll()
                        // 允许匿名访问AI聊天接口
                        .requestMatchers("/api/ai/chat").permitAll()
                        // 允许匿名访问用户画像接口（临时用于测试）
                        .requestMatchers("/api/user/portrait/**").permitAll()
                        // 允许匿名访问新闻接口
                        .requestMatchers("/api/news/**").permitAll()
                        // 允许匿名访问学习资源接口
                        .requestMatchers("/api/learning/**").permitAll()
                        // 允许匿名访问通知公告接口
                        .requestMatchers("/api/notices/**").permitAll()
                        // 允许匿名访问活动接口
                        .requestMatchers("/api/activities/**").permitAll()
                        // 允许匿名访问首页推荐活动接口
                        .requestMatchers("/api/home/activities").permitAll()
                        // 允许匿名访问首页推荐内容接口
                        .requestMatchers("/api/home/recommended").permitAll()
                        // 允许匿名访问首页头条新闻接口
                        .requestMatchers("/api/home/headline-news").permitAll()
                        // 允许匿名访问样板支部接口
                        .requestMatchers("/api/branches/model/**").permitAll()
                        // 允许匿名访问就业服务接口
                        .requestMatchers("/api/employment/**").permitAll()
                        // 允许匿名访问会话统计接口
                        .requestMatchers("/api/session-stats/**").permitAll()
                        // 其他路径需要认证
                        .anyRequest().authenticated()
                )
                // 配置认证异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("未授权访问");
                        })
                );

        // 添加JWT认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt密码编码器
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}