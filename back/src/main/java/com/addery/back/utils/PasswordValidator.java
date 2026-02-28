package com.addery.back.utils;

import java.util.regex.Pattern;

/**
 * 密码验证工具类
 */
public class PasswordValidator {

    // 密码正则表达式：至少包含一个大写字母，一个小写字母，一个数字和一个特殊字符，长度6-20位
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * 验证密码强度
     * @param password 密码
     * @return 是否符合强度要求
     */
    public static boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

    /**
     * 获取密码强度要求提示
     * @return 密码强度要求
     */
    public static String getPasswordRequirements() {
        return "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符(@$!%*?&)，长度6-20位";
    }
}
