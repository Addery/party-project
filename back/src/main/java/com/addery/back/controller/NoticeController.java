package com.addery.back.controller;

import com.addery.back.dto.common.Response;
import com.addery.back.dto.notice.HomeNoticeDTO;
import com.addery.back.entity.Notice;
import com.addery.back.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 获取公告列表（分页，支持分类和关键词搜索）
     * @param page 当前页码（从1开始）
     * @param size 每页大小
     * @param sortField 排序字段
     * @param sortDirection 排序方向（asc/desc）
     * @param categoryId 分类ID（可选）
     * @param keyword 搜索关键词（可选）
     * @return 分页公告列表
     */
    @GetMapping
    public Response<Page<Notice>> getNoticeList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "publishDate") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword) {
        try {
            // 转换为0-based页码
            Pageable pageable = PageRequest.of(
                    page - 1,
                    size,
                    Sort.by(Sort.Direction.fromString(sortDirection), sortField)
            );
            // 获取分类名称
            String category = null;
            if (categoryId != null) {
                // 使用NoticeServiceImpl中的分类映射
                category = noticeService.getCategoryById(categoryId);
            }
            Page<Notice> noticePage = noticeService.getNoticesByCategoryAndKeyword(category, keyword, pageable);
            return Response.success("获取公告列表成功", noticePage);
        } catch (Exception e) {
            System.out.println("[ERROR] NoticeController.getNoticeList 异常: " + e.getMessage());
            e.printStackTrace();
            return Response.error("获取公告列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取公告详情
     * @param id 公告ID
     * @return 公告详情
     */
    @GetMapping("/{id}")
    public Response<Notice> getNoticeById(@PathVariable Integer id) {
        try {
            Optional<Notice> notice = noticeService.getNoticeById(id);
            if (notice.isPresent()) {
                // 增加浏览次数
                noticeService.incrementViews(id);
                return Response.success("获取公告详情成功", notice.get());
            } else {
                return Response.error("公告不存在");
            }
        } catch (Exception e) {
            return Response.error("获取公告详情失败");
        }
    }

    /**
     * 获取最新公告
     * @param limit 数量限制
     * @return 最新公告列表
     */
    @GetMapping("/latest")
    public Response<List<Notice>> getLatestNotices(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishDate"));
            List<Notice> latestNotices = noticeService.getAllNotices(pageable).getContent();
            return Response.success("获取最新公告成功", latestNotices);
        } catch (Exception e) {
            return Response.error("获取最新公告失败");
        }
    }

    /**
     * 获取首页通知公告
     * @param limit 数量限制
     * @return 首页通知公告列表
     */
    @GetMapping("/home")
    public Response<List<HomeNoticeDTO>> getHomeNotices(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<HomeNoticeDTO> homeNotices = noticeService.getHomeNotices(limit);
            return Response.success("获取首页通知公告成功", homeNotices);
        } catch (Exception e) {
            return Response.error("获取首页通知公告失败");
        }
    }
}
