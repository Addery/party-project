package com.addery.back.config;

import com.addery.back.entity.Branch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;

/**
 * JPA属性转换器：将List<Branch.BranchActivity>转换为JSON字符串，用于数据库存储 */
@Converter(autoApply = true)
public class BranchActivityListToJsonConverter implements AttributeConverter<List<Branch.BranchActivity>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Branch.BranchActivity> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert List<BranchActivity> to JSON", e);
        }
    }

    @Override
    public List<Branch.BranchActivity> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(dbData, new com.fasterxml.jackson.core.type.TypeReference<List<Branch.BranchActivity>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to List<BranchActivity>", e);
        }
    }
}

