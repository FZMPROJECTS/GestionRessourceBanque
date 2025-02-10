package com.example.gestionressourcebanque.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String loginPage(){
        return "auth-login";
    }

    @RequestMapping("/home")
    public String loginSubmit(){
        return "/pages/CreateDemande";
    }
    @RequestMapping("/erreur")
    public String showErrorPage(ModelMap modelMap) {
        return "/pages/Erreur";
    }

}
