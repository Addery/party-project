package com.addery.back.repository;

import com.addery.back.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    List<Attachment> findByRelatedIdAndType(Integer relatedId, String type);
}
