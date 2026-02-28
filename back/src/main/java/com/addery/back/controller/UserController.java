package com.addery.back.controller;

import com.addery.back.dto.common.Response;
import com.addery.back.entity.User;
import com.addery.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     * @return 当前用户信息
     */
    @GetMapping("/current")
    public Response<User> getCurrentUser() {
        try {
            User user = userService.getCurrentUser();
            if (user != null) {
                // 隐藏密码
                user.setPassword(null);
                return Response.success("获取用户信息成功", user);
            } else {
                return Response.error("未找到用户信息");
            }
        } catch (Exception e) {
            return Response.error("获取用户信息失败");
        }
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Integer id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                // 隐藏密码
                user.get().setPassword(null);
                return Response.success("获取用户信息成功", user.get());
            } else {
                return Response.error("用户不存在");
            }
        } catch (Exception e) {
            return Response.error("获取用户信息失败");
        }
    }

    /**
     * 更新当前用户个人资料
     * @param updateRequest 更新请求参数
     * @return 更新后的用户信息
     */
    @PutMapping("/profile")
    public Response<User> updateProfile(@RequestBody Map<String, Object> updateRequest) {
        try {
            User updatedUser = userService.updateCurrentUserProfile(updateRequest);
            // 隐藏密码
            updatedUser.setPassword(null);
            return Response.success("更新个人资料成功", updatedUser);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        } catch (Exception e) {
            return Response.error("更新个人资料失败");
        }
    }
}
