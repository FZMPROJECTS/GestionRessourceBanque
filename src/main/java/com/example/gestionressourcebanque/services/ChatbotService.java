package com.example.gestionressourcebanque.services;

import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final ChatbotPdfService chatbotPdfService;

    public ChatbotService(ChatbotPdfService chatbotPdfService) {
        this.chatbotPdfService = chatbotPdfService;
    }

    public String getResponse(String message) {
        return chatbotPdfService.getResponse(message);
    }
}
