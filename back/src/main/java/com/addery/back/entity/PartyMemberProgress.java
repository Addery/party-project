package com.addery.back.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "party_member_progress")
public class PartyMemberProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "progress_type", nullable = false, length = 50)
    private String progressType;
    
    @Column(name = "progress_title", length = 100)
    private String progressTitle;
    
    @Column(name = "progress_date")
    private Date progressDate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 50)
    private String status;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
}

