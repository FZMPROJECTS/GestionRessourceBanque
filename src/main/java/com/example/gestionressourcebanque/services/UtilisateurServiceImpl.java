package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateurById(long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public void deleteAllUtilisateurs() {
        utilisateurRepository.deleteAll();
    }

    @Override
    public Utilisateur getUtilisateurById(long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public long getTotalUtilisateurs() {
        return utilisateurRepository.countTotalUtilisateurs();
    }

    @Override
    public double getPercentageAdministrateurs() {
        long totalUtilisateurs = getTotalUtilisateurs();

        // Obtenir les utilisateurs ayant le rôle "Administrateur" ou "ADMIN"
        List<Utilisateur> utilisateursAvecRoles = utilisateurRepository.findByRoles_NameIn(Arrays.asList("ADMINISTRATEUR", "ADMIN"));

        // Utiliser un Set pour éliminer les doublons
        Set<Utilisateur> administrateursUniques = new HashSet<>(utilisateursAvecRoles);

        long totalAdministrateurs = administrateursUniques.size();

        if (totalUtilisateurs == 0) return 0;

        // Calculer le pourcentage et arrondir au plus proche entier
        return Math.round((totalAdministrateurs / (double) totalUtilisateurs) * 100);
    }


    @Override
    public double getPercentageUsers() {
        long totalUtilisateurs = getTotalUtilisateurs();

        // Obtenir les utilisateurs ayant le rôle "Utilisateur" ou "USER"
        List<Utilisateur> utilisateursAvecRoles = utilisateurRepository.findByRoles_NameIn(Arrays.asList("UTILISATEUR", "USER"));

        // Utiliser un Set pour éliminer les doublons
        Set<Utilisateur> utilisateursUniques = new HashSet<>(utilisateursAvecRoles);

        long totalUsers = utilisateursUniques.size();

        if (totalUtilisateurs == 0) return 0;

        // Calculer le pourcentage et arrondir au plus proche entier
        return Math.round((totalUsers / (double) totalUtilisateurs) * 100);
    }


    @Override
    public Utilisateur getUtilisateurByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }

    @Override
    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}