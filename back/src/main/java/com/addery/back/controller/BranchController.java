package com.addery.back.controller;

import com.addery.back.dto.branch.BranchDetailDTO;
import com.addery.back.dto.branch.BranchListItemDTO;
import com.addery.back.dto.common.Response;
import com.addery.back.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    @Autowired
    private BranchService branchService;

    /**
     * 获取样板支部列表
     * @param limit 每页显示数量，默认6条
     * @param sortBy 排序字段，可选值：createdAt、name，默认createdAt
     * @param order 排序顺序，可选值：asc、desc，默认desc
     * @return 样板支部列表
     */
    @GetMapping("/model")
    public Response<List<BranchListItemDTO>> getModelBranches(
            @RequestParam(defaultValue = "6") Integer limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        try {
            List<BranchListItemDTO> branches = branchService.getModelBranches(limit, sortBy, order);
            return Response.success("获取样板支部列表成功", branches);
        } catch (Exception e) {
            return Response.error("获取样板支部列表失败");
        }
    }

    /**
     * 根据ID获取样板支部详情
     * @param id 样板支部ID
     * @return 样板支部详情
     */
    @GetMapping("/{id:\\d+}")
    public Response<BranchDetailDTO> getModelBranchById(@PathVariable Integer id) {
        try {
            BranchDetailDTO branch = branchService.getModelBranchById(id);
            if (branch != null) {
                return Response.success("获取样板支部详情成功", branch);
            } else {
                return Response.error("样板支部不存在");
            }
        } catch (Exception e) {
            return Response.error("获取样板支部详情失败");
        }
    }
}
