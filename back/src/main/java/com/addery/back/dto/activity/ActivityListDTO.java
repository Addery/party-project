package com.addery.back.dto.activity;

import java.util.List;

public class ActivityListDTO {
    private List<ActivityItemDTO> activities;
    private Long total;
    private Integer pages;
    private Integer page;
    private Integer pageSize;

    public List<ActivityItemDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityItemDTO> activities) {
        this.activities = activities;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
