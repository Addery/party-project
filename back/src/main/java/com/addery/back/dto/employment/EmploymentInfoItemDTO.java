package com.addery.back.dto.employment;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentInfoItemDTO {
    private Integer id;
    private String title;
    private Date publishDate;
    private Integer views;
    private String category;
    private String source;
}
