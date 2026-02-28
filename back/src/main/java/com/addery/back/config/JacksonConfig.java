package com.addery.back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 全局Jackson配置类
 * 用于统一设置JSON序列化格式，特别是日期格式
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        
        // 设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        
        // 禁用时间戳格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 创建自定义序列化模块
        SimpleModule module = new SimpleModule();
        
        // 配置Date类型的序列化器，返回时使用"yyyy年MM月dd日"格式
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        module.addSerializer(Date.class, new DateSerializer(null, outputDateFormat));
        
        // 配置LocalDate类型的序列化器，返回时使用"yyyy年MM月dd日"格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        module.addSerializer(LocalDate.class, new LocalDateSerializer(outputFormatter));
        
        // 注册模块
        objectMapper.registerModule(module);
        
        return objectMapper;
    }
}
