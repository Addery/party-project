package com.addery.back.controller;

import com.addery.back.dto.*;

import com.addery.back.dto.common.Response;
import com.addery.back.dto.employment.*;
import com.addery.back.service.EmploymentApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    @Autowired
    private EmploymentApiService employmentApiService;

    /**
     * 获取就业资讯详情
     * @param id 就业资讯ID
     * @return 就业资讯详情
     */
    @GetMapping("/detail/{id}")
    public Response<EmploymentInfoDetailDTO> getEmploymentInfoDetail(@PathVariable Integer id) {
        try {
            EmploymentInfoDetailDTO result = employmentApiService.getEmploymentInfoDetail(id);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取企业招聘信息列表
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param category 分类，可选
     * @param isHot 是否热门，可选
     * @return 企业招聘信息列表
     */
    @GetMapping("/recruitment")
    public Response<EmploymentInfoListDTO> getRecruitmentInfoList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isHot) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getRecruitmentInfoList(page, pageSize, category, isHot);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取就业统计数据
     * @return 就业统计数据
     */
    @GetMapping("/stats")
    public Response<EmploymentStatsDTO> getEmploymentStats() {
        try {
            EmploymentStatsDTO result = employmentApiService.getEmploymentStats();
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取就业资讯列表
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param category 分类，可选
     * @param isHot 是否热门，可选
     * @return 就业资讯列表
     */
    @GetMapping("/news")
    public Response<EmploymentInfoListDTO> getEmploymentNewsList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isHot) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getEmploymentNewsList(page, pageSize, category, isHot);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取就业指导资源列表
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param type 指导类型，可选
     * @return 就业指导资源列表
     */
    @GetMapping("/guidance")
    public Response<EmploymentGuidanceListDTO> getEmploymentGuidanceList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String type) {
        try {
            EmploymentGuidanceListDTO result = employmentApiService.getEmploymentGuidanceList(page, pageSize, type);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取老师指导学生就业情况
     * @param type 指导类型，可选
     * @return 老师指导学生就业情况列表
     */
    @GetMapping("/graduate")
    public Response<List<TeacherEmploymentGuidanceDTO>> getTeacherEmploymentGuidanceList(
            @RequestParam(required = false) String type) {
        try {
            List<TeacherEmploymentGuidanceDTO> result = employmentApiService.getTeacherEmploymentGuidanceList(type);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取就业政策列表
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @return 就业政策列表
     */
    @GetMapping("/policies")
    public Response<EmploymentPolicyListDTO> getEmploymentPolicyList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentPolicyListDTO result = employmentApiService.getEmploymentPolicyList(page, pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取就业服务列表
     * @return 就业服务列表
     */
    @GetMapping("/services")
    public Response<List<EmploymentServiceDTO>> getEmploymentServiceList() {
        try {
            List<EmploymentServiceDTO> result = employmentApiService.getEmploymentServiceList();
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    // 企业招聘相关接口
    /**
     * 获取热门职位
     * @param pageSize 每页数量，可选
     * @return 热门职位列表
     */
    @GetMapping("/recruitment/hot")
    public Response<EmploymentInfoListDTO> getHotRecruitment(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getHotRecruitment(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取最新招聘
     * @param pageSize 每页数量，可选
     * @return 最新招聘信息列表
     */
    @GetMapping("/recruitment/latest")
    public Response<EmploymentInfoListDTO> getLatestRecruitment(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getLatestRecruitment(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取相关招聘
     * @param id 当前招聘ID
     * @param pageSize 每页数量，可选
     * @return 相关招聘信息列表
     */
    @GetMapping("/recruitment/related/{id}")
    public Response<EmploymentInfoListDTO> getRelatedRecruitment(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getRelatedRecruitment(id, pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取企业更多职位
     * @param companyId 企业ID
     * @param pageSize 每页数量，可选
     * @return 企业更多职位列表
     */
    @GetMapping("/recruitment/company/{companyId}")
    public Response<EmploymentInfoListDTO> getCompanyRecruitment(
            @PathVariable Integer companyId,
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getCompanyRecruitment(companyId, pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    // 就业指导相关接口
    /**
     * 获取热门指导资源
     * @param pageSize 每页数量，可选
     * @return 热门指导资源列表
     */
    @GetMapping("/guidance/hot")
    public Response<EmploymentGuidanceListDTO> getHotEmploymentGuidance(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentGuidanceListDTO result = employmentApiService.getHotEmploymentGuidance(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取最新指导资源
     * @param pageSize 每页数量，可选
     * @return 最新指导资源列表
     */
    @GetMapping("/guidance/latest")
    public Response<EmploymentGuidanceListDTO> getLatestEmploymentGuidance(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentGuidanceListDTO result = employmentApiService.getLatestEmploymentGuidance(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    // 就业政策相关接口
    /**
     * 获取热门就业政策
     * @param pageSize 每页数量，可选
     * @return 热门就业政策列表
     */
    @GetMapping("/policies/hot")
    public Response<EmploymentPolicyListDTO> getHotEmploymentPolicies(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentPolicyListDTO result = employmentApiService.getHotEmploymentPolicies(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    // 就业服务相关接口
    /**
     * 获取分页就业服务列表
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param keyword 关键词，可选
     * @param serviceType 服务类型，可选
     * @param serviceObject 服务对象，可选
     * @return 分页就业服务列表
     */
    @GetMapping("/services/page")
    public Response<EmploymentServiceListDTO> getPageEmploymentServices(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String serviceObject) {
        try {
            EmploymentServiceListDTO result = employmentApiService.getPageEmploymentServices(page, pageSize, keyword, serviceType, serviceObject);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取热门就业服务
     * @param pageSize 每页数量，可选
     * @return 热门就业服务列表
     */
    @GetMapping("/services/hot")
    public Response<EmploymentServiceListDTO> getHotEmploymentServices(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentServiceListDTO result = employmentApiService.getHotEmploymentServices(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
    
    // 就业资讯相关接口
    /**
     * 获取热门就业资讯
     * @param pageSize 每页数量，可选
     * @return 热门就业资讯列表
     */
    @GetMapping("/news/hot")
    public Response<EmploymentInfoListDTO> getHotEmploymentNews(
            @RequestParam(required = false) Integer pageSize) {
        try {
            EmploymentInfoListDTO result = employmentApiService.getHotEmploymentNews(pageSize);
            return Response.success("获取成功", result);
        } catch (Exception e) {
            return Response.error("获取失败：" + e.getMessage());
        }
    }
}
