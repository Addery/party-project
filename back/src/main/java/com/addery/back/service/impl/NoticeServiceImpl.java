package com.addery.back.service.impl;

import com.addery.back.dto.common.AttachmentDTO;
import com.addery.back.dto.notice.HomeNoticeDTO;
import com.addery.back.dto.notice.NoticeDetailDTO;
import com.addery.back.dto.notice.NoticeItemDTO;
import com.addery.back.dto.notice.NoticeListDTO;
import com.addery.back.entity.Attachment;
import com.addery.back.entity.Notice;
import com.addery.back.repository.AttachmentRepository;
import com.addery.back.repository.NoticeRepository;
import com.addery.back.service.NoticeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;
    
    @Autowired
    private AttachmentRepository attachmentRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // 分类ID到分类名称的映射
    private static final Map<Integer, String> CATEGORY_MAPPING = new HashMap<>();
    private static final Map<String, Integer> REVERSE_CATEGORY_MAPPING = new HashMap<>();
    
    static {
        CATEGORY_MAPPING.put(1, "工作通知");
        CATEGORY_MAPPING.put(2, "学习通知");
        CATEGORY_MAPPING.put(3, "会议通知");
        CATEGORY_MAPPING.put(4, "放假通知");
        CATEGORY_MAPPING.put(5, "活动通知");
        CATEGORY_MAPPING.put(6, "其他通知");
        
        // 初始化反向映射
        for (Map.Entry<Integer, String> entry : CATEGORY_MAPPING.entrySet()) {
            REVERSE_CATEGORY_MAPPING.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public List<HomeNoticeDTO> getHomeNotices(Integer limit) {
        if (limit == null) {
            limit = 5;
        }

        Pageable pageable = PageRequest.of(0, limit);
        Page<Notice> noticePage = noticeRepository.findAllByOrderByPublishDateDesc(pageable);

        List<HomeNoticeDTO> result = new ArrayList<>();
        for (Notice notice : noticePage.getContent()) {
            HomeNoticeDTO dto = new HomeNoticeDTO();
            dto.setId(notice.getId());
            dto.setTitle(notice.getTitle());
            dto.setDepartment(notice.getDepartment());
            if (notice.getPublishDate() != null) {
                dto.setDate(dateFormat.format(notice.getPublishDate()));
            }
            result.add(dto);
        }

        return result;
    }

    @Override
    public NoticeListDTO getNoticeList(Integer page, Integer pageSize, Integer categoryId, String keyword) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Notice> noticePage;

        // 使用分类ID获取对应的分类名称
        String category = null;
        if (categoryId != null) {
            category = CATEGORY_MAPPING.get(categoryId);
        }

        // 当没有keyword时，传递一个空字符串，这样可以重用findByCategoryAndKeyword方法
        if (keyword == null) {
            keyword = "";
        }
        noticePage = noticeRepository.findByCategoryAndKeyword(category, keyword, pageable);

        NoticeListDTO result = new NoticeListDTO();
        result.setTotal(noticePage.getTotalElements());
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setPages(noticePage.getTotalPages());

        List<NoticeItemDTO> items = new ArrayList<>();
        for (Notice notice : noticePage.getContent()) {
            NoticeItemDTO item = new NoticeItemDTO();
            item.setId(notice.getId());
            item.setTitle(notice.getTitle());
            item.setCategory(notice.getCategory());
            // 设置分类ID
            item.setCategoryId(REVERSE_CATEGORY_MAPPING.get(notice.getCategory()));
            item.setSource(notice.getDepartment()); // 使用department字段作为source
            if (notice.getPublishDate() != null) {
                item.setPublishDate(dateFormat.format(notice.getPublishDate()));
            }
            items.add(item);
        }

        result.setNotices(items);
        return result;
    }

    @Override
    public NoticeDetailDTO getNoticeDetail(Integer id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        if (notice == null) {
            return null;
        }

        NoticeDetailDTO result = new NoticeDetailDTO();
        result.setId(notice.getId());
        result.setTitle(notice.getTitle());
        result.setCategory(notice.getCategory());
        result.setSource(notice.getDepartment()); // 使用department字段作为source
        result.setContent(notice.getContent());
        if (notice.getPublishDate() != null) {
            result.setPublishDate(dateFormat.format(notice.getPublishDate()));
        }

        // 处理附件
        List<Attachment> attachmentList = attachmentRepository.findByRelatedIdAndType(notice.getId(), "notice");
        List<AttachmentDTO> attachments = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            AttachmentDTO dto = new AttachmentDTO();
            dto.setName(attachment.getFileName());
            dto.setUrl(attachment.getFilePath());
            attachments.add(dto);
        }
        result.setAttachments(attachments);

        return result;
    }

    @Override
    public List<String> getNoticeCategories() {
        return noticeRepository.findAllCategories();
    }

    @Override
    public Page<Notice> getAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    @Override
    public Optional<Notice> getNoticeById(Integer id) {
        return noticeRepository.findById(id);
    }

    @Override
    @Transactional
    public void incrementViews(Integer id) {
        noticeRepository.incrementViews(id);
    }
    
    @Override
    public Page<Notice> getNoticesByCategoryAndKeyword(String category, String keyword, Pageable pageable) {
        // 当没有keyword时，传递一个空字符串，这样可以重用findByCategoryAndKeyword方法
        if (keyword == null) {
            keyword = "";
        }
        return noticeRepository.findByCategoryAndKeyword(category, keyword, pageable);
    }
    
    @Override
    public String getCategoryById(Integer categoryId) {
        // 使用分类映射获取分类名称
        return CATEGORY_MAPPING.get(categoryId);
    }
}