package com.addery.back.dto.employment;

import lombok.Data;

@Data
public class EmploymentStatsDTO {
    private Float employmentRate;
    private Float graduateEmploymentRate;
    private Integer companyCount;
    private Integer recruitmentCount;
}
