package com.addery.back.service;

import com.addery.back.dto.employment.*;
import java.util.List;

public interface EmploymentApiService {
    // 获取就业统计数据
    EmploymentStatsDTO getEmploymentStats();

    // 获取就业资讯详情
    EmploymentInfoDetailDTO getEmploymentInfoDetail(Integer id);

    // 获取就业资讯列表
    EmploymentInfoListDTO getEmploymentNewsList(Integer page, Integer pageSize, String category, Boolean isHot);

    // 获取企业招聘信息列表
    EmploymentInfoListDTO getRecruitmentInfoList(Integer page, Integer pageSize, String category, Boolean isHot);
    
    // 企业招聘相关方法
    EmploymentInfoListDTO getHotRecruitment(Integer pageSize);
    EmploymentInfoListDTO getLatestRecruitment(Integer pageSize);
    EmploymentInfoListDTO getRelatedRecruitment(Integer id, Integer pageSize);
    EmploymentInfoListDTO getCompanyRecruitment(Integer companyId, Integer pageSize);

    // 获取就业指导资源列表
    EmploymentGuidanceListDTO getEmploymentGuidanceList(Integer page, Integer pageSize, String type);
    
    // 就业指导相关方法
    EmploymentGuidanceListDTO getHotEmploymentGuidance(Integer pageSize);
    EmploymentGuidanceListDTO getLatestEmploymentGuidance(Integer pageSize);

    // 获取老师指导学生就业情况
    List<TeacherEmploymentGuidanceDTO> getTeacherEmploymentGuidanceList(String type);

    // 获取就业政策列表
    EmploymentPolicyListDTO getEmploymentPolicyList(Integer page, Integer pageSize);
    
    // 获取就业政策详情
    EmploymentPolicyDetailDTO getEmploymentPolicyDetail(Integer id);
    
    // 就业政策相关方法
    EmploymentPolicyListDTO getHotEmploymentPolicies(Integer pageSize);

    // 获取就业服务列表
    List<EmploymentServiceDTO> getEmploymentServiceList();
    
    // 就业服务相关方法
    EmploymentServiceListDTO getPageEmploymentServices(Integer page, Integer pageSize, String keyword, String serviceType, String serviceObject);
    EmploymentServiceListDTO getHotEmploymentServices(Integer pageSize);
    
    // 就业资讯相关方法
    EmploymentInfoListDTO getHotEmploymentNews(Integer pageSize);
}
