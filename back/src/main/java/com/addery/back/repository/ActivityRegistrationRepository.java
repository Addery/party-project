package com.addery.back.repository;

import com.addery.back.entity.ActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Integer> {
    Optional<ActivityRegistration> findByActivityIdAndUserId(Integer activityId, Integer userId);
}
