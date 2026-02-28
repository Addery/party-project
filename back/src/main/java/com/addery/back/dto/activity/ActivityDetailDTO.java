package com.addery.back.dto.activity;

import java.util.List;
import com.addery.back.dto.common.AttachmentDTO;

public class ActivityDetailDTO {
    private Integer id;
    private String title;
    private String date;
    private String time;
    private String location;
    private String category;
    private String status;
    private String description;
    private String content;
    private Boolean canRegister;
    private String registerDeadline;
    private Integer participants;
    private Integer capacity;
    private Integer views;
    private String image;
    private List<AttachmentDTO> attachments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCanRegister() {
        return canRegister;
    }

    public void setCanRegister(Boolean canRegister) {
        this.canRegister = canRegister;
    }

    public String getRegisterDeadline() {
        return registerDeadline;
    }

    public void setRegisterDeadline(String registerDeadline) {
        this.registerDeadline = registerDeadline;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }
}
