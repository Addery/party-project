package com.addery.back.dto.employment;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentPolicyItemDTO {
    private Integer id;
    private String title;
    private String category;
    private String source;
    private Date publishDate;
    private Integer views;
    private String summary;
}
