package com.addery.back.dto.common;

import lombok.Data;

@Data
public class CategoryDTO {
    private String id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
