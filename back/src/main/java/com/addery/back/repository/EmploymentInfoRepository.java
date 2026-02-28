package com.addery.back.repository;

import com.addery.back.entity.EmploymentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EmploymentInfoRepository extends JpaRepository<EmploymentInfo, Integer> {
    Page<EmploymentInfo> findByCategory(String category, Pageable pageable);
    
    @Query("SELECT e FROM EmploymentInfo e WHERE e.isHot = true")
    Page<EmploymentInfo> findHotEmploymentInfo(Pageable pageable);
    
    @Query("SELECT e FROM EmploymentInfo e WHERE e.category = :category AND e.isHot = true")
    Page<EmploymentInfo> findHotEmploymentInfoByCategory(@Param("category") String category, Pageable pageable);
    
    Page<EmploymentInfo> findByCategoryAndIdNot(String category, Integer id, Pageable pageable);
    
    // 根据category列表查询就业信息
    Page<EmploymentInfo> findByCategoryIn(List<String> categories, Pageable pageable);
    
    // 根据category列表查询热门就业信息
    @Query("SELECT e FROM EmploymentInfo e WHERE e.category IN :categories AND e.isHot = true")
    Page<EmploymentInfo> findHotEmploymentInfoByCategoryIn(@Param("categories") List<String> categories, Pageable pageable);
}
