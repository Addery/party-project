package com.addery.back.repository;

import com.addery.back.entity.EmploymentGuidance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentGuidanceRepository extends JpaRepository<EmploymentGuidance, Integer> {
    Page<EmploymentGuidance> findByType(String type, Pageable pageable);
}
