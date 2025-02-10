package com.example.gestionressourcebanque.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatbotPageController {

    @GetMapping("/chatbot")
    public String getChatbotPage() {
        return "/pages/Chatbot";
    }
}
