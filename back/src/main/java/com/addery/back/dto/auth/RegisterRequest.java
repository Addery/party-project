package com.addery.back.dto.auth;

import com.addery.back.entity.User;
import jakarta.validation.constraints.*;

import java.util.Date;
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private User.Gender gender;

    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 100, message = "年龄不能大于100岁")
    private Integer age;

    private User.PartyStatus partyStatus;

    private String branch;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$", message = "身份证号格式不正确")
    private String idCard;

    @Past(message = "入党日期不能是未来日期")
    private Date joinDate;

    private String position;

    private User.Education education;

    @Min(value = 0, message = "工作年限不能为负数")
    private Integer workYears;

    private String avatar;

    private User.Role role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User.Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public User.PartyStatus getPartyStatus() {
        return partyStatus;
    }

    public String getBranch() {
        return branch;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getIdCard() {
        return idCard;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getPosition() {
        return position;
    }

    public User.Education getEducation() {
        return education;
    }

    public Integer getWorkYears() {
        return workYears;
    }

    public String getAvatar() {
        return avatar;
    }

    public User.Role getRole() {
        return role;
    }
}