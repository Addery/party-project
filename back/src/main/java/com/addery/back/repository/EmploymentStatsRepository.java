package com.addery.back.repository;

import com.addery.back.entity.EmploymentStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentStatsRepository extends JpaRepository<EmploymentStats, Integer> {
}
