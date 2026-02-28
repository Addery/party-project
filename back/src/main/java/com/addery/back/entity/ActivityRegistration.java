package com.addery.back.entity;

import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "activity_registrations")
public class ActivityRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "registration_time")
    private Date registrationTime;
    
    @Column(length = 50)
    private String status;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public Activity getActivity() {
        return activity;
    }

    public Integer getUserId() {
        return userId;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
