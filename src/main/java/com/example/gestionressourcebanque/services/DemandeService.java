package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Demande;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.enums.StatutDemande;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface DemandeService {

    Demande creerDemande(Utilisateur utilisateur, Materiel materiel);
    List<Demande> getDemandesEnCours();
    public List<Demande> getDemandesRejeter();
    public List<Demande> getDemandesConfirmer();

    Demande traiterDemande(Long demandeId, StatutDemande statutDemande);

    Demande saveDemande(Demande demande);
    Demande updateDemande(Demande demande);
    void deleteDemandeById(long id);
    void deleteAllDemandes();
    Demande getDemandeById(long id);
    List<Demande> getAllDemandes();

    Map<String, Long> getDemandeCountsByDateRange(LocalDate start, LocalDate end);

    List<Demande> getDemandesForToday();

    Long getDemandeCountForCurrentMonth();

    // Méthode pour obtenir les demandes par utilisateur et statut
    List<Demande> getDemandesByUtilisateurAndStatut(Long utilisateurId, StatutDemande statut);

    List<Demande> getDemandesForUser(String username);
    Demande getMateriel(Materiel materiel);
}
