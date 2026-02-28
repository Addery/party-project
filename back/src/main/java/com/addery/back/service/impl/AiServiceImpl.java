package com.addery.back.service;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Classname AiService
 * @Description TODO
 * <p>
 * @Date 2025/12/6 11:03
 * @Created by 14121
 * @Author Addery
 * @Version 1.0
 */
@Service
public class AiServiceImpl implements AiService {

    @Value("${ark.api.key}")
    private String apiKey;

    @Value("${ark.base.url}")
    private String baseUrl;

    @Value("${ark.model.id}")
    private String modelId;

    public String getAiResponse(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Input message is empty.";
        }

        ArkService arkService = null;
        try {
            arkService = ArkService.builder()
                    .apiKey(apiKey)
                    .baseUrl(baseUrl)
                    .build();

            // 👇 添加系统提示：设定 AI 行为
            ChatMessage systemMessage = ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content("请简要回答以下问题：")
                    .build();

            // 用户消息
            ChatMessage userMessageObj = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(userMessage.trim())
                    .build();

            // 消息列表：先系统，再用户
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(systemMessage);
            messages.add(userMessageObj);


            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(modelId)
                    .messages(messages)
                    .build();

            var response = arkService.createChatCompletion(request);

            if (response.getChoices() == null || response.getChoices().isEmpty()) {
                return "No response from AI service.";
            }

            Object content = response.getChoices().get(0).getMessage().getContent();
            return extractTextFromContent(content);

        } catch (Exception e) {
            // 记录日志（生产环境应使用 SLF4J）
//            System.err.println("AI service error: " + e.getMessage());
//            e.printStackTrace();
            return "Sorry, I encountered an error: " + e.getMessage();
        } finally {
            if (arkService != null) {
                arkService.shutdownExecutor(); // 释放资源
            }
        }
    }

    // 安全提取文本内容（兼容 String 和多模态 List）
    private String extractTextFromContent(Object content) {
        if (content == null) {
            return "";
        }

        if (content instanceof String) {
            return (String) content;
        }

        if (content instanceof List<?>) {
            StringBuilder sb = new StringBuilder();
            @SuppressWarnings("unchecked")
            List<Object> items = (List<Object>) content;
            for (Object item : items) {
                if (item instanceof InputContentItemText) {
                    sb.append(((InputContentItemText) item).getText());
                }
                // 忽略图片等非文本内容
            }
            return sb.toString();
        }

        // 兜底：转字符串（不推荐，但防崩溃）
        return content.toString();
    }

}
