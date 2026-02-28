package com.addery.back.service;

import com.addery.back.dto.branch.BranchDetailDTO;
import com.addery.back.dto.branch.BranchListItemDTO;
import java.util.List;

public interface BranchService {
    /**
     * 获取样板支部列表
     * @param limit 每页显示数量，默认6条
     * @param sortBy 排序字段，可选值：createdAt、name，默认createdAt
     * @param order 排序顺序，可选值：asc、desc，默认desc
     * @return 样板支部列表
     */
    List<BranchListItemDTO> getModelBranches(Integer limit, String sortBy, String order);

    /**
     * 根据ID获取样板支部详情
     * @param id 样板支部ID
     * @return 样板支部详情
     */
    BranchDetailDTO getModelBranchById(Integer id);
}