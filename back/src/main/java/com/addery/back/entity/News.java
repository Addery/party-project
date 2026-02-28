package com.addery.back.entity;

import com.addery.back.enums.NewsCategoryEnum;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 200)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private NewsCategoryEnum category;
    
    @Column(length = 50)
    private String author;
    
    @Column(name = "publish_date")
    private Date publishDate;
    

    
    @Column(name = "cover_image")
    private String coverImage;
    
    @Column(name = "views_count")
    private Integer viewsCount;
    
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "is_top")
    private Boolean isTop;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
}