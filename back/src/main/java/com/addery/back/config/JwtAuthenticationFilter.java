package com.addery.back.config;

import com.addery.back.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于验证JWT令牌并设置认证上下文
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 记录请求信息，便于调试
            System.out.println("Processing request: " + request.getMethod() + " " + request.getRequestURI());
            
            // 从请求头中获取Authorization头
            String authorizationHeader = request.getHeader("Authorization");
            System.out.println("Authorization header: " + authorizationHeader);

            // 验证Authorization头是否存在且格式正确
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // 提取JWT令牌（去掉"Bearer "前缀）
                String token = authorizationHeader.substring(7);

                // 验证令牌并获取用户名
                String username = jwtUtils.getUsernameFromToken(token);
                Integer userId = jwtUtils.getUserIdFromToken(token);
                System.out.println("Token username: " + username + ", userId: " + userId);

                // 如果令牌有效且用户未被认证
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 加载用户详情
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 验证令牌
                    if (jwtUtils.validateToken(token)) {
                        // 创建认证令牌
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 设置认证上下文
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("Authentication set for user: " + username);
                    } else {
                        System.out.println("Invalid token");
                    }
                }
            } else {
                System.out.println("No valid authorization header found");
            }
        } catch (Exception e) {
            // 如果JWT验证失败，记录错误并继续处理请求
            System.out.println("JWT filter exception: " + e.getMessage());
            e.printStackTrace();
            // 这样permitAll的接口仍然可以访问
        }

        // 继续处理请求
        System.out.println("Continuing filter chain for request: " + request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}