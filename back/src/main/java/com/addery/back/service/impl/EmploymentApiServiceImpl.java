package com.addery.back.service.impl;

import com.addery.back.dto.employment.*;
import com.addery.back.entity.*;
import com.addery.back.repository.*;
import com.addery.back.service.EmploymentApiService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmploymentApiServiceImpl implements EmploymentApiService {

    @Autowired
    private EmploymentInfoRepository employmentInfoRepository;

    @Autowired
    private EmploymentPolicyRepository employmentPolicyRepository;

    @Autowired
    private EmploymentGuidanceRepository employmentGuidanceRepository;

    @Autowired
    private EmploymentServiceRepository employmentServiceRepository;

    @Autowired
    private TeacherEmploymentGuidanceRepository teacherEmploymentGuidanceRepository;

    @Autowired
    private EmploymentStatsRepository employmentStatsRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public EmploymentInfoDetailDTO getEmploymentInfoDetail(Integer id) {
        EmploymentInfo employmentInfo = employmentInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("就业资讯不存在"));
        return modelMapper.map(employmentInfo, EmploymentInfoDetailDTO.class);
    }

    @Override
    public EmploymentInfoListDTO getRecruitmentInfoList(Integer page, Integer pageSize, String category, Boolean isHot) {
        // 设置默认值
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Pageable pageable = PageRequest.of(currentPage - 1, size, Sort.by(Sort.Direction.DESC, "publishDate"));

        // 查询数据 - 只返回招聘相关的信息（根据category字段区分）
        Page<EmploymentInfo> employmentInfoPage;
        // 如果用户指定了category，则使用用户指定的category
        if (category != null && !category.isEmpty()) {
            if (isHot != null && isHot) {
                employmentInfoPage = employmentInfoRepository.findHotEmploymentInfoByCategory(category, pageable);
            } else {
                employmentInfoPage = employmentInfoRepository.findByCategory(category, pageable);
            }
        } else {
            // 如果用户没有指定category，则默认查询招聘相关的信息
            // 根据SQL脚本中的数据，招聘相关的category包括'校园招聘'、'IT互联网'等
            if (isHot != null && isHot) {
                employmentInfoPage = employmentInfoRepository.findHotEmploymentInfoByCategoryIn(
                        Arrays.asList("校园招聘", "IT互联网", "企业招聘", "recruitment"), pageable);
            } else {
                employmentInfoPage = employmentInfoRepository.findByCategoryIn(
                        Arrays.asList("校园招聘", "IT互联网", "企业招聘", "recruitment"), pageable);
            }
        }

        // 转换为DTO
        List<EmploymentInfoItemDTO> list = employmentInfoPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());

        // 构建返回结果
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(employmentInfoPage.getTotalElements());
        result.setPage(currentPage);
        result.setPageSize(size);

        return result;
    }

    @Override
    public EmploymentGuidanceListDTO getEmploymentGuidanceList(Integer page, Integer pageSize, String type) {
        // 设置默认值
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Pageable pageable = PageRequest.of(currentPage - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 查询数据
        Page<EmploymentGuidance> guidancePage;
        if (type != null && !type.isEmpty()) {
            guidancePage = employmentGuidanceRepository.findByType(type, pageable);
        } else {
            guidancePage = employmentGuidanceRepository.findAll(pageable);
        }

        // 转换为DTO
        List<EmploymentGuidanceItemDTO> list = guidancePage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentGuidanceItemDTO.class))
                .collect(Collectors.toList());

        // 构建返回结果
        EmploymentGuidanceListDTO result = new EmploymentGuidanceListDTO();
        result.setList(list);
        result.setTotal(guidancePage.getTotalElements());
        result.setPage(currentPage);
        result.setPageSize(size);

        return result;
    }

    @Override
    public List<TeacherEmploymentGuidanceDTO> getTeacherEmploymentGuidanceList(String type) {
        List<TeacherEmploymentGuidance> guidanceList;
        if (type != null && !type.isEmpty()) {
            guidanceList = teacherEmploymentGuidanceRepository.findByType(type);
        } else {
            guidanceList = teacherEmploymentGuidanceRepository.findAll();
        }

        return guidanceList.stream()
                .map(item -> modelMapper.map(item, TeacherEmploymentGuidanceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmploymentPolicyListDTO getEmploymentPolicyList(Integer page, Integer pageSize) {
        // 设置默认值
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Pageable pageable = PageRequest.of(currentPage - 1, size, Sort.by(Sort.Direction.DESC, "publishDate"));

        // 查询数据
        Page<EmploymentPolicy> policyPage = employmentPolicyRepository.findAll(pageable);

        // 转换为DTO
        List<EmploymentPolicyItemDTO> list = policyPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentPolicyItemDTO.class))
                .collect(Collectors.toList());

        // 构建返回结果
        EmploymentPolicyListDTO result = new EmploymentPolicyListDTO();
        result.setList(list);
        result.setTotal(policyPage.getTotalElements());
        result.setPage(currentPage);
        result.setPageSize(size);

        return result;
    }

    @Override
    public EmploymentPolicyDetailDTO getEmploymentPolicyDetail(Integer id) {
        EmploymentPolicy employmentPolicy = employmentPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("就业政策不存在"));
        return modelMapper.map(employmentPolicy, EmploymentPolicyDetailDTO.class);
    }

    @Override
    public List<EmploymentServiceDTO> getEmploymentServiceList() {
        List<EmploymentService> serviceList = employmentServiceRepository.findAll();

        return serviceList.stream()
                .map(item -> modelMapper.map(item, EmploymentServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmploymentStatsDTO getEmploymentStats() {
        // 获取最新的就业统计数据（按id降序排序，取第一条）
        EmploymentStats latestStats = employmentStatsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("就业统计数据不存在"));

        // 直接获取就业率（数据库中已存储为百分比形式），并处理可能的null值
        float employmentRate = latestStats.getEmploymentRate() != null ? latestStats.getEmploymentRate().floatValue() : 0.0f;

        // 直接从数据库获取毕业生就业率（数据库中已存储为百分比形式），并处理可能的null值
        float graduateEmploymentRate = latestStats.getGraduateEmploymentRate() != null ? latestStats.getGraduateEmploymentRate().floatValue() : 0.0f;

        // 创建并返回DTO
        EmploymentStatsDTO statsDTO = new EmploymentStatsDTO();
        statsDTO.setEmploymentRate(employmentRate);
        statsDTO.setGraduateEmploymentRate(graduateEmploymentRate);
        statsDTO.setCompanyCount(latestStats.getCompanyCount());
        statsDTO.setRecruitmentCount(latestStats.getRecruitmentCount());

        // 其他统计数据需要从相应的repository中查询并计算
        // 这里我们暂时只返回数据库中已有的字段，其他字段可以根据实际需求添加查询逻辑

        return statsDTO;
    }

    @Override
    public EmploymentInfoListDTO getEmploymentNewsList(Integer page, Integer pageSize, String category, Boolean isHot) {
        // 设置默认值
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Pageable pageable = PageRequest.of(currentPage - 1, size, Sort.by(Sort.Direction.DESC, "publishDate"));

        // 查询数据
        Page<EmploymentInfo> employmentInfoPage;
        if (isHot != null && isHot) {
            if (category != null && !category.isEmpty()) {
                employmentInfoPage = employmentInfoRepository.findHotEmploymentInfoByCategory(category, pageable);
            } else {
                employmentInfoPage = employmentInfoRepository.findHotEmploymentInfo(pageable);
            }
        } else {
            if (category != null && !category.isEmpty()) {
                employmentInfoPage = employmentInfoRepository.findByCategory(category, pageable);
            } else {
                employmentInfoPage = employmentInfoRepository.findAll(pageable);
            }
        }

        // 转换为DTO
        List<EmploymentInfoItemDTO> list = employmentInfoPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());

        // 构建返回结果
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(employmentInfoPage.getTotalElements());
        result.setPage(currentPage);
        result.setPageSize(size);

        return result;
    }

    // 企业招聘相关方法实现
    @Override
    public EmploymentInfoListDTO getHotRecruitment(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "viewCount"));
        Page<EmploymentInfo> recruitmentPage = employmentInfoRepository.findHotEmploymentInfo(pageable);
        
        List<EmploymentInfoItemDTO> list = recruitmentPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(recruitmentPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    @Override
    public EmploymentInfoListDTO getLatestRecruitment(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<EmploymentInfo> recruitmentPage = employmentInfoRepository.findAll(pageable);
        
        List<EmploymentInfoItemDTO> list = recruitmentPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(recruitmentPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    @Override
    public EmploymentInfoListDTO getRelatedRecruitment(Integer id, Integer pageSize) {
        // 先获取当前招聘信息
        EmploymentInfo currentRecruitment = employmentInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("招聘信息不存在"));
        
        // 获取相同类别的招聘信息，排除当前ID
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<EmploymentInfo> relatedRecruitmentPage = employmentInfoRepository.findByCategoryAndIdNot(
                currentRecruitment.getCategory(), id, pageable);
        
        List<EmploymentInfoItemDTO> list = relatedRecruitmentPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(relatedRecruitmentPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    @Override
    public EmploymentInfoListDTO getCompanyRecruitment(Integer companyId, Integer pageSize) {
        // 注意：当前EmploymentInfo实体类中没有companyId字段，这里使用category字段作为临时替代
        // 实际项目中需要在EmploymentInfo实体类中添加companyId字段
        String companyCategory = "company_" + companyId;
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<EmploymentInfo> companyRecruitmentPage = employmentInfoRepository.findByCategory(companyCategory, pageable);
        
        List<EmploymentInfoItemDTO> list = companyRecruitmentPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(companyRecruitmentPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    // 就业指导相关方法实现
    @Override
    public EmploymentGuidanceListDTO getHotEmploymentGuidance(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<EmploymentGuidance> guidancePage = employmentGuidanceRepository.findAll(pageable);
        
        List<EmploymentGuidanceItemDTO> list = guidancePage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentGuidanceItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentGuidanceListDTO result = new EmploymentGuidanceListDTO();
        result.setList(list);
        result.setTotal(guidancePage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    @Override
    public EmploymentGuidanceListDTO getLatestEmploymentGuidance(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<EmploymentGuidance> guidancePage = employmentGuidanceRepository.findAll(pageable);
        
        List<EmploymentGuidanceItemDTO> list = guidancePage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentGuidanceItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentGuidanceListDTO result = new EmploymentGuidanceListDTO();
        result.setList(list);
        result.setTotal(guidancePage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    // 就业政策相关方法实现
    @Override
    public EmploymentPolicyListDTO getHotEmploymentPolicies(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<EmploymentPolicy> policyPage = employmentPolicyRepository.findAll(pageable);
        
        List<EmploymentPolicyItemDTO> list = policyPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentPolicyItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentPolicyListDTO result = new EmploymentPolicyListDTO();
        result.setList(list);
        result.setTotal(policyPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    // 就业服务相关方法实现
    @Override
    public EmploymentServiceListDTO getPageEmploymentServices(Integer page, Integer pageSize, String keyword, String serviceType, String serviceObject) {
        // 设置默认值
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Pageable pageable = PageRequest.of(currentPage - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        // 查询数据
        Page<EmploymentService> servicePage;
        // 注意：当前EmploymentService实体类中没有serviceType和serviceObject字段，这里暂时使用全部查询
        // 实际项目中需要在EmploymentService实体类中添加这些字段并实现相应的查询方法
        servicePage = employmentServiceRepository.findAll(pageable);

        // 转换为DTO
        List<EmploymentServiceDTO> list = servicePage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentServiceDTO.class))
                .collect(Collectors.toList());

        // 构建返回结果
        EmploymentServiceListDTO result = new EmploymentServiceListDTO();
        result.setList(list);
        result.setTotal(servicePage.getTotalElements());
        result.setPage(currentPage);
        result.setPageSize(size);

        return result;
    }

    @Override
    public EmploymentServiceListDTO getHotEmploymentServices(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<EmploymentService> servicePage = employmentServiceRepository.findAll(pageable);
        
        List<EmploymentServiceDTO> list = servicePage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentServiceDTO.class))
                .collect(Collectors.toList());
        
        EmploymentServiceListDTO result = new EmploymentServiceListDTO();
        result.setList(list);
        result.setTotal(servicePage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }

    // 就业资讯相关方法实现
    @Override
    public EmploymentInfoListDTO getHotEmploymentNews(Integer pageSize) {
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "viewCount"));
        Page<EmploymentInfo> newsPage = employmentInfoRepository.findHotEmploymentInfo(pageable);
        
        List<EmploymentInfoItemDTO> list = newsPage.getContent().stream()
                .map(item -> modelMapper.map(item, EmploymentInfoItemDTO.class))
                .collect(Collectors.toList());
        
        EmploymentInfoListDTO result = new EmploymentInfoListDTO();
        result.setList(list);
        result.setTotal(newsPage.getTotalElements());
        result.setPage(1);
        result.setPageSize(size);
        
        return result;
    }
}
