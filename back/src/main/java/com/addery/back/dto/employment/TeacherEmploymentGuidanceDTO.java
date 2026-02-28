package com.addery.back.dto.employment;

import lombok.Data;

@Data
public class TeacherEmploymentGuidanceDTO {
    private Integer id;
    private String name;
    private Integer studentCount;
    private Integer rankChange;
    private String type;
}
