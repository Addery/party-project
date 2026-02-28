package com.addery.back.service.impl;

import com.addery.back.dto.auth.LoginRequest;
import com.addery.back.dto.auth.RegisterRequest;
import com.addery.back.entity.User;
import com.addery.back.repository.RegistrationWhitelistRepository;
import com.addery.back.repository.UserRepository;
import com.addery.back.service.UserService;
import com.addery.back.utils.JwtUtils;
import com.addery.back.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationWhitelistRepository whitelistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 注册新用户
     * @param registerRequest 注册请求参数
     * @return 注册成功的用户信息
     */
    @Override
    public User register(RegisterRequest registerRequest) {
        // 白名单验证：检查手机号和身份证号组合是否在白名单中
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isEmpty() &&
                registerRequest.getIdCard() != null && !registerRequest.getIdCard().isEmpty()) {
            boolean isInWhitelist = whitelistRepository.findByPhoneAndIdCard(
                    registerRequest.getPhone(), registerRequest.getIdCard()).isPresent();
            if (!isInWhitelist) {
                throw new RuntimeException("该手机号和身份证号组合不在注册白名单中");
            }
        } else {
            throw new RuntimeException("注册需要提供手机号和身份证号");
        }

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty() && 
            userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 检查手机号是否已存在
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isEmpty() && 
            userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }

        // 验证密码强度
        if (!PasswordValidator.isValid(registerRequest.getPassword())) {
            throw new RuntimeException(PasswordValidator.getPasswordRequirements());
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        // 密码加密
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        // 设置默认值
        user.setGender(registerRequest.getGender() != null ? registerRequest.getGender() : User.Gender.男);
        user.setAge(registerRequest.getAge());
        user.setPartyStatus(registerRequest.getPartyStatus() != null ? registerRequest.getPartyStatus() : User.PartyStatus.入党申请人);
        user.setBranch(registerRequest.getBranch() != null ? registerRequest.getBranch() : "默认支部");
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setIdCard(registerRequest.getIdCard());
        user.setJoinDate(registerRequest.getJoinDate());
        user.setPosition(registerRequest.getPosition());
        user.setEducation(registerRequest.getEducation());
        user.setWorkYears(registerRequest.getWorkYears());
        user.setAvatar(registerRequest.getAvatar());
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : User.Role.member);
        user.setStatus(User.Status.active);

        // 保存用户
        return userRepository.save(user);
    }

    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return 登录结果，包含JWT令牌和用户信息
     */
    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // 设置认证上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取用户信息
        User user = (User) authentication.getPrincipal();

        // 生成JWT令牌（使用用户名和用户ID）
        String token = jwtUtils.generateToken(user.getUsername(), user.getId());

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return result;
    }

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 获取当前登录用户信息
     * @return 当前登录用户信息
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }
    
    @Override
    public User updateCurrentUserProfile(Map<String, Object> updateRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("未找到登录用户信息");
        }

        // 更新用户信息（根据updateRequest中的字段进行选择性更新）
        updateRequest.forEach((key, value) -> {
            switch (key) {
                case "nickname":
                    currentUser.setName((String) value);
                    break;
                case "email":
                    currentUser.setEmail((String) value);
                    break;
                case "phone":
                    currentUser.setPhone((String) value);
                    break;
                case "gender":
                    currentUser.setGender(User.Gender.values()[(Integer) value]);
                    break;
                case "avatar":
                    currentUser.setAvatar((String) value);
                    break;

                // 可以根据User实体类的字段继续添加其他可更新的字段
            }
        });

        // 保存更新后的用户信息
        return userRepository.save(currentUser);
    }
}