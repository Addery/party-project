package com.addery.back.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_behavior")
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(length = 50)
    private String username;
    
    @Column(name = "behavior_type", nullable = false, length = 50)
    private String behaviorType;
    
    @Column(name = "behavior_content")
    private String behaviorContent;
    
    @Column(name = "content_category", length = 50)
    private String contentCategory;
    
    @Column(name = "content_type", length = 50)
    private String contentType;
    
    @Column(name = "behavior_time")
    private Date behaviorTime;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(length = 50)
    private String device;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }

    public String getBehaviorContent() {
        return behaviorContent;
    }

    public void setBehaviorContent(String behaviorContent) {
        this.behaviorContent = behaviorContent;
    }

    public String getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(String contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getBehaviorTime() {
        return behaviorTime;
    }

    public void setBehaviorTime(Date behaviorTime) {
        this.behaviorTime = behaviorTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}