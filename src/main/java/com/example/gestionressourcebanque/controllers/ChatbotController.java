package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.ChatMessage;
import com.example.gestionressourcebanque.entities.ChatResponse;
import com.example.gestionressourcebanque.services.ChatbotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/message")
    public ChatResponse handleMessage(@RequestBody ChatMessage chatMessage) {
        System.out.println("Message reçu : " + chatMessage.getMessage());
        String response = chatbotService.getResponse(chatMessage.getMessage());
        return new ChatResponse(response);
    }

}
