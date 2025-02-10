package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Demande;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.enums.StatutDemande;
import com.example.gestionressourcebanque.repositories.DemandeRepository;
import com.example.gestionressourcebanque.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.gestionressourcebanque.enums.StatutDemande.*;

@Service
@AllArgsConstructor
public class DemandeServiceImpl implements DemandeService{
    @Autowired
    private DemandeRepository demandeRepository ;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Demande creerDemande(Utilisateur utilisateur, Materiel materiel) {
        Demande demande = new Demande();
        demande.setUtilisateur(utilisateur);
        demande.setMateriel(materiel);
        demande.setStatut(En_Cours);
        return demandeRepository.save(demande);
    }
    @Override
    public List<Demande> getDemandesEnCours() {
        return demandeRepository.findByStatut(En_Cours);
    }
    @Override
    public List<Demande> getDemandesRejeter() {
        return demandeRepository.findByStatut(Rejeter);
    }
    @Override
    public List<Demande> getDemandesConfirmer() {
        return demandeRepository.findByStatut(Confirmer);
    }
    @Override
    public Demande traiterDemande(Long demandeId, StatutDemande statutDemande) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        demande.setStatut(statutDemande);
        return demandeRepository.save(demande);
    }

    @Override
    public Map<String, Long> getDemandeCountsByDateRange(LocalDate start, LocalDate end) {
        Map<String, Long> demandesByDate = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Populate all dates in the range with 0 counts
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            demandesByDate.put(date.format(formatter), 0L);
        }

        // Query the database to get the actual counts
        List<Object[]> results = demandeRepository.countDemandesByDateRange(start, end);
        for (Object[] result : results) {
            if (result[0] instanceof LocalDate) {
                LocalDate date = (LocalDate) result[0];
                Long count = (Long) result[1];
                demandesByDate.put(date.format(formatter), count);
            }
        }

        return demandesByDate;
    }

    @Override
    public List<Demande> getDemandesForToday() {
        return demandeRepository.findDemandesForToday();

    }

    @Override
    public Long getDemandeCountForCurrentMonth() {
        return demandeRepository.countDemandesForCurrentMonth();
    }


    @Override
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande updateDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public void deleteDemandeById(long id) {
        demandeRepository.deleteById(id);
    }

    @Override
    public void deleteAllDemandes() {
        demandeRepository.deleteAll();
    }

    @Override
    public Demande getDemandeById(long id) {
        return demandeRepository.findById(id).get();
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> getDemandesByUtilisateurAndStatut(Long utilisateurId, StatutDemande statut) {
        return demandeRepository.findDemandesByUtilisateurAndStatut(utilisateurId, statut);
    }

    @Override
    public List<Demande> getDemandesForUser(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        return demandeRepository.findByUtilisateur(utilisateur);
    }

    @Override
    public Demande getMateriel(Materiel materiel) {
        return demandeRepository.findByMateriel(materiel);
    }


}
