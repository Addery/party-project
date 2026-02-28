package com.addery.back.repository;

import com.addery.back.entity.EmploymentPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentPolicyRepository extends JpaRepository<EmploymentPolicy, Integer> {
}
