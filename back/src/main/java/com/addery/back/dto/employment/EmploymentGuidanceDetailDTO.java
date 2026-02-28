package com.addery.back.dto.employment;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentGuidanceDetailDTO {
    private Integer id;
    private String title;
    private String category;
    private String author;
    private Date publishDate;
    private String content;
    private Integer views;
    private String coverImage;
    private String relatedLinks;
}
