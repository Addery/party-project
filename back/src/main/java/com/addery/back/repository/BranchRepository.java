package com.addery.back.repository;

import com.addery.back.entity.Branch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    // 查询所有激活的样板支部
    @Query("SELECT b FROM Branch b WHERE b.isActive = :isActive")
    List<Branch> findByActive(@Param("isActive") Boolean isActive, Pageable pageable);
    
    // 查询所有激活的样板支部（带排序）
    @Query("SELECT b FROM Branch b WHERE b.isActive = :isActive")
    List<Branch> findByActive(@Param("isActive") Boolean isActive, Sort sort);
    
    // 根据ID查询激活的样板支部
    @Query("SELECT b FROM Branch b WHERE b.id = :id AND b.isActive = :isActive")
    Optional<Branch> findByIdAndActive(@Param("id") Integer id, @Param("isActive") Boolean isActive);
}