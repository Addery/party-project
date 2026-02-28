package com.addery.back.repository;

import com.addery.back.entity.EmploymentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentServiceRepository extends JpaRepository<EmploymentService, Integer> {
}
