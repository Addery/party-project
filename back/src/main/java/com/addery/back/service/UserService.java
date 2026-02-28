package com.addery.back.service;

import com.addery.back.dto.auth.LoginRequest;
import com.addery.back.dto.auth.RegisterRequest;
import com.addery.back.entity.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    // 用户注册
    User register(RegisterRequest registerRequest);
    
    // 用户登录
    Map<String, Object> login(LoginRequest loginRequest);
    
    // 根据用户名查找用户
    Optional<User> getUserByUsername(String username);
    
    // 获取当前登录用户
    User getCurrentUser();
    
    // 更新当前登录用户信息
    User updateCurrentUserProfile(Map<String, Object> updateRequest);
    
    // 根据ID获取用户信息
    Optional<User> getUserById(Integer id);
}