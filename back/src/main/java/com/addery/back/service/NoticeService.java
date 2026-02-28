package com.addery.back.service;

import com.addery.back.dto.notice.HomeNoticeDTO;
import com.addery.back.dto.notice.NoticeDetailDTO;
import com.addery.back.dto.notice.NoticeListDTO;
import com.addery.back.entity.Notice;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface NoticeService {
    // 获取首页显示的最新通知公告列表
    List<HomeNoticeDTO> getHomeNotices(Integer limit);

    // 获取分页的通知公告列表
    NoticeListDTO getNoticeList(Integer page, Integer pageSize, Integer categoryId, String keyword);

    // 获取指定通知公告的详细信息
    NoticeDetailDTO getNoticeDetail(Integer id);
    
    // 获取通知公告分类列表
    List<String> getNoticeCategories();

    Page<Notice> getAllNotices(Pageable pageable);

    Optional<Notice> getNoticeById(Integer id);

    void incrementViews(Integer id);
    
    // 根据分类和关键词获取公告列表
    Page<Notice> getNoticesByCategoryAndKeyword(String category, String keyword, Pageable pageable);
    
    // 根据分类ID获取分类名称
    String getCategoryById(Integer categoryId);
}