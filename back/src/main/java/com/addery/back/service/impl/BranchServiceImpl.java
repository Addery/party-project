package com.addery.back.service.impl;

import com.addery.back.dto.branch.BranchDetailDTO;
import com.addery.back.dto.branch.BranchListItemDTO;
import com.addery.back.entity.Branch;
import com.addery.back.repository.BranchRepository;
import com.addery.back.service.BranchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<BranchListItemDTO> getModelBranches(Integer limit, String sortBy, String order) {
        // 设置默认值
        if (limit == null || limit <= 0 || limit > 20) {
            limit = 6;
        }
        if (sortBy == null || (!sortBy.equals("createdAt") && !sortBy.equals("name"))) {
            sortBy = "createdAt";
        }
        if (order == null || (!order.equals("asc") && !order.equals("desc"))) {
            order = "desc";
        }

        // 创建排序规则
        Sort.Direction direction = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // 获取数据并转换为DTO，只获取激活的样板支部
        return branchRepository.findByActive(true, PageRequest.of(0, limit, sort))
                .stream()
                .map(this::convertToListItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDetailDTO getModelBranchById(Integer id) {
        Branch branch = branchRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new NoSuchElementException("样板支部不存在"));
        return convertToDetailDTO(branch);
    }

    // 转换为列表项DTO
    private BranchListItemDTO convertToListItemDTO(Branch branch) {
        BranchListItemDTO dto = new BranchListItemDTO();
        BeanUtils.copyProperties(branch, dto);
        return dto;
    }

    // 转换为详情DTO
    private BranchDetailDTO convertToDetailDTO(Branch branch) {
        BranchDetailDTO dto = new BranchDetailDTO();
        BeanUtils.copyProperties(branch, dto);
        return dto;
    }
}