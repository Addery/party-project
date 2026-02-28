package com.addery.back.dto.employment;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentGuidanceItemDTO {
    private Integer id;
    private String title;
    private String type;
    private Date createdAt;
}
