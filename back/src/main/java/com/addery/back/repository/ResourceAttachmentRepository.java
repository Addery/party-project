package com.addery.back.repository;

import com.addery.back.entity.ResourceAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceAttachmentRepository extends JpaRepository<ResourceAttachment, Integer> {
    
}
