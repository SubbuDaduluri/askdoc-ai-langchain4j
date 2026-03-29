package com.subbu.askdoc.controller;


import com.subbu.askdoc.dto.ChatHistoryResponse;
import com.subbu.askdoc.dto.ChatRequest;
import com.subbu.askdoc.dto.ChatResponse;
import com.subbu.askdoc.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Chat endpoint using JSON request
     */
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String result = chatService.chat(request.getSessionId(), request.getMessage());

        return new ChatResponse(result);
    }

}