package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Notification;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.services.NotificationService;
import com.example.gestionressourcebanque.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute("userNotifications")
    public List<Notification> getUserNotifications() {
        System.out.println("Début de getUserNotifications");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Est authentifié: " + auth.isAuthenticated());

        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println("Username récupéré: " + username);

        Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username);
        System.out.println("Utilisateur trouvé: " + (utilisateur != null ? utilisateur.getUsername() : "null"));

        if (utilisateur != null) {
            System.out.println("ID de l'utilisateur: " + utilisateur.getId());
            List<Notification> notifications = notificationService.getNotifications(utilisateur.getId());
            System.out.println("Notifications pour l'utilisateur " + utilisateur.getUsername() + ": " + notifications.size());
            return notifications != null ? notifications : new ArrayList<>();
        } else {
            System.out.println("Aucun utilisateur trouvé, retour d'une liste vide");
            return new ArrayList<>();
        }
    }
}