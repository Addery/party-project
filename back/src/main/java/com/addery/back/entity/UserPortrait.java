package com.addery.back.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user_portrait")
public class UserPortrait {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "political_outlook", length = 50)
    private String politicalOutlook;
    
    @Column(name = "party_branch", length = 100)
    private String partyBranch;
    
    @Column(name = "join_party_date")
    private Date joinPartyDate;
    
    @Column(name = "party_years")
    private Integer partyYears;
    
    @Column(length = 50)
    private String education;
    
    @Column(length = 100)
    private String position;
    
    @Column(name = "work_unit", length = 100)
    private String workUnit;
    
    @Column(name = "learning_hours")
    private Integer learningHours;
    
    @Column(name = "course_completions")
    private Integer courseCompletions;
    
    @Column(name = "activity_participation")
    private Integer activityParticipation;
    
    @Column(name = "last_learning_date")
    private Date lastLearningDate;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
}

