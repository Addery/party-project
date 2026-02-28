package com.addery.back.controller;

import com.addery.back.dto.auth.LoginRequest;
import com.addery.back.dto.auth.RegisterRequest;
import com.addery.back.dto.common.Response;
import com.addery.back.entity.User;
import com.addery.back.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param registerRequest 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Response<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.register(registerRequest);
            // 隐藏密码
            user.setPassword(null);
            return Response.success("注册成功", user);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        } catch (Exception e) {
            return Response.error("注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return 登录结果，包含JWT令牌和用户信息
     */
    @PostMapping("/login")
    public Response<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> result = userService.login(loginRequest);
            // 隐藏密码
            User user = (User) result.get("user");
            user.setPassword(null);
            return Response.success("登录成功", result);
        } catch (RuntimeException e) {
            return Response.error("用户名或密码错误");
        } catch (Exception e) {
            return Response.error("登录失败，请稍后重试");
        }
    }
}