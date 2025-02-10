package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.entities.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UtilisateurService {
    Utilisateur saveUtilisateur (Utilisateur utilisateur);
    Utilisateur updateUtilisateur(Utilisateur utilisateur);
    void deleteUtilisateurById(long id);
    void deleteAllUtilisateurs();
    Utilisateur getUtilisateurById(long id);
    List<Utilisateur> getAllUtilisateurs();

    // Nouvelle méthode pour obtenir le nombre total d'utilisateurs
    public long getTotalUtilisateurs();

    public double getPercentageAdministrateurs();

    public double getPercentageUsers();

    Utilisateur getUtilisateurByUsername(String username);

    String encryptPassword(String plainPassword);
}
