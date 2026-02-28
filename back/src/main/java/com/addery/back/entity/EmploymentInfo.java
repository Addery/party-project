package com.addery.back.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "employment_info")
public class EmploymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "publish_date")
    private Date publishDate;

    private String source;

    @Column(name = "view_count")
    private Integer viewCount;

    private String category;

    @Column(name = "is_hot")
    private Boolean isHot;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
