package com.addery.back.dto.branch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.addery.back.entity.Branch;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BranchDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private String image;
    private String level;
    private String introduction;
    // 暂时注释，解决启动问题
    // private List<String> achievements;
    private List<Branch.BranchActivity> activities;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
}