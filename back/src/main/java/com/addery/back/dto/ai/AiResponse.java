package com.addery.back.dto;

/**
 * @Classname AiResponse
 * @Description TODO
 * <p>
 * @Date 2025/12/6 11:03
 * @Created by 14121
 * @Author Addery
 * @Version 1.0
 */
public class AiResponse {
    private String reply;

    public AiResponse(String reply) {
        this.reply = reply;
    }

    // Getter
    public String getReply() {
        return reply;
    }
}