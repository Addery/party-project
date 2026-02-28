package com.addery.back.dto.branch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BranchListItemDTO {
    private Integer id;
    private String name;
    private String description;
    private String image;
    private String level;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
}