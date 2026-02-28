package com.addery.back.dto.notice;

import java.util.List;

public class NoticeListDTO {
    private Long total;
    private Integer page;
    private Integer pageSize;
    private Integer pages;
    private List<NoticeItemDTO> notices;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<NoticeItemDTO> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticeItemDTO> notices) {
        this.notices = notices;
    }
}