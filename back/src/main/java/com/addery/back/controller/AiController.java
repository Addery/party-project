package com.addery.back.controller;

import com.addery.back.dto.AiRequest;
import com.addery.back.dto.AiResponse;
import com.addery.back.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname AiController
 * @Description TODO
 * <p>
 * @Date 2025/12/6 11:04
 * @Created by 14121
 * @Author Addery
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    public AiResponse chat(@RequestBody AiRequest request) {
        String reply = aiService.getAiResponse(request.getMessage());
        return new AiResponse(reply);
    }
}
