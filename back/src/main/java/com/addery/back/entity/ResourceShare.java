package com.addery.back.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resource_shares")
public class ResourceShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private LearningResource resource;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "share_platform", length = 50)
    private String sharePlatform;
    
    @Column(name = "share_link", length = 500)
    private String shareLink;
    
    @Column(name = "share_time")
    private Date shareTime;
    
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

    public LearningResource getResource() {
        return resource;
    }

    public void setResource(LearningResource resource) {
        this.resource = resource;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSharePlatform() {
        return sharePlatform;
    }

    public void setSharePlatform(String sharePlatform) {
        this.sharePlatform = sharePlatform;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
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