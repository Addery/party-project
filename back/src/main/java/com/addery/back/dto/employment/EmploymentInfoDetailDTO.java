package com.addery.back.dto.employment;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentInfoDetailDTO {
    private Integer id;
    private String title;
    private String content;
    private Date publishDate;
    private String publisher;
    private Integer views;
    private String category;
    private String source;
}
