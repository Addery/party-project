package com.addery.back.repository;

import com.addery.back.entity.TeacherEmploymentGuidance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeacherEmploymentGuidanceRepository extends JpaRepository<TeacherEmploymentGuidance, Integer> {
    List<TeacherEmploymentGuidance> findByType(String type);
}
