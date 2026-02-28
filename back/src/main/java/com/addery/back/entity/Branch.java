package com.addery.back.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 200)
    private String address;
    
    @Column(length = 50)
    private String contactPerson;
    
    @Column(length = 20)
    private String contactPhone;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "member_count")
    private Integer memberCount;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(length = 200)
    private String image;
    
    @Column(length = 20)
    private String level;
    
    @Column(columnDefinition = "TEXT")
    private String introduction;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    // 暂时注释，解决启动问题
    // @ElementCollection
    // @CollectionTable(name = "branch_achievements", joinColumns = @JoinColumn(name = "branch_id"))
    // @Column(name = "achievement", columnDefinition = "TEXT")
    // private List<String> achievements;
    
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members;
    
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities;

    // 内部类：支部活动信息
    @Data
    public static class BranchActivity {
        private Integer activityId;
        private String activityName;
        private Date activityDate;
        private String activityLocation;
    }
}