package com.example.gestionressourcebanque.securite.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class SecurityController {

    @GetMapping("/home")
    public String home(Authentication authentication) {

        // Récupère les rôles de l'utilisateur authentifié
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Redirige les administrateurs vers la racine
        if (roles.contains("ROLE_ADMIN")||roles.contains("ROLE_ADMINISTRATEUR")) {
            return "redirect:/";
        }
        // Redirige les utilisateurs vers leur page d'accueil spécifique
        else if (roles.contains("ROLE_USER")||roles.contains("ROLE_UTILISATEUR")) {
            return "/pages/home";
        }

        // Par défaut, redirige vers la page de login
        return "redirect:/login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "/pages/AccessDenied";
    }

    @GetMapping("/login")
    public String login() {
        return "/pages/login";
    }
}
