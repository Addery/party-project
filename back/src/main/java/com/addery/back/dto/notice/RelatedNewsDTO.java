package com.addery.back.dto.notice;

import lombok.Data;

import java.util.Date;

/**
 * 相关新闻DTO类
 * 只包含前端需要的字段：id、title、publishDate、category
 */
@Data
public class RelatedNewsDTO {
    private Integer id;
    private String title;
    private Date publishDate;
    private String category;

    /**
     * 构造方法，用于从News对象创建RelatedNewsDTO
     * @param id 新闻ID
     * @param title 新闻标题
     * @param publishDate 发布日期
     * @param category 新闻分类
     */
    public RelatedNewsDTO(Integer id, String title, Date publishDate, String category) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.category = category;
    }
}
