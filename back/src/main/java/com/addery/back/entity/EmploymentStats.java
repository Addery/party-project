package com.addery.back.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "employment_stats")
public class EmploymentStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employment_rate")
    private Double employmentRate;

    @Column(name = "graduate_employment_rate")
    private Double graduateEmploymentRate;

    @Column(name = "company_count")
    private Integer companyCount;

    @Column(name = "recruitment_count")
    private Integer recruitmentCount;

    private Integer year;

    private Integer semester;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
