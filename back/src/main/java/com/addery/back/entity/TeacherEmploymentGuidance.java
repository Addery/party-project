package com.addery.back.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "teacher_employment_guidance")
public class TeacherEmploymentGuidance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "student_count")
    private Integer studentCount;

    @Column(name = "rank_change")
    private Integer rankChange;

    private String type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
