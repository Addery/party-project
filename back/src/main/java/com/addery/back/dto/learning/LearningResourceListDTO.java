package com.addery.back.dto.learning;

import java.util.List;

public class LearningResourceListDTO {
    private List<LearningResourceItemDTO> resources;
    private Long total;
    private Integer pages;
    private Integer page;
    private Integer pageSize;

    public List<LearningResourceItemDTO> getResources() {
        return resources;
    }

    public void setResources(List<LearningResourceItemDTO> resources) {
        this.resources = resources;
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
