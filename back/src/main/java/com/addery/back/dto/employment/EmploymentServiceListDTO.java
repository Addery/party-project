package com.addery.back.dto.employment;

import java.util.List;

public class EmploymentServiceListDTO {
    private List<EmploymentServiceDTO> list;
    private Long total;
    private Integer page;
    private Integer pageSize;

    public List<EmploymentServiceDTO> getList() {
        return list;
    }

    public void setList(List<EmploymentServiceDTO> list) {
        this.list = list;
    }

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
}
