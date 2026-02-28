package com.addery.back.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "party_member_portrait")
public class PartyMemberPortrait {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(length = 50)
    private String username;
    
    @Column(name = "party_years")
    private Integer partyYears;
    
    @Column(length = 100)
    private String education;
    
    @Column(length = 100)
    private String job;
    
    @Column(length = 100)
    private String partyStatus;
    
    @Column(length = 100)
    private String gender;
    
    @Column(length = 100)
    private String age;
    
    @Column(name = "party_branch", length = 100)
    private String branchName;
    
    @Column(name = "learning_hours")
    private Integer learningHours;
    
    @Column(name = "course_completions")
    private Integer courseCompletions;
    
    @Column(name = "activity_participation")
    private Integer activityParticipation;
    
    @Column(name = "volunteer_hours")
    private Integer volunteerHours;
    
    @Column(name = "award_count")
    private Integer awardCount;
    
    @Column(name = "evaluation_score")
    private Double evaluationScore;
    
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
}

