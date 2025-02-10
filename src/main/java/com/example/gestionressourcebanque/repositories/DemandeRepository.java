package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Demande;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande,Long> {
    List<Demande> findByStatut(StatutDemande statut);
    @Query("SELECT d.dateDemande, COUNT(d) FROM Demande d WHERE d.dateDemande BETWEEN :start AND :end GROUP BY d.dateDemande")
    List<Object[]> countDemandesByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT d FROM Demande d WHERE DATE(d.dateDemande) = CURRENT_DATE")
    List<Demande> findDemandesForToday();

    @Query("SELECT COUNT(d) FROM Demande d WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE) AND MONTH(d.dateDemande) = MONTH(CURRENT_DATE)")
    Long countDemandesForCurrentMonth();

    // Méthode pour obtenir les demandes par utilisateur et statut
    @Query("SELECT d FROM Demande d WHERE d.utilisateur.id = :utilisateurId AND d.statut = :statut")
    List<Demande> findDemandesByUtilisateurAndStatut(@Param("utilisateurId") Long utilisateurId, @Param("statut") StatutDemande statut);

    List<Demande> findByUtilisateur(Utilisateur utilisateur);
    @Modifying
    @Transactional
    @Query("DELETE FROM Demande d WHERE d.materiel.id = :materielId")
    void deleteByMaterielId(@Param("materielId") long materielId);

    Demande findByMateriel(Materiel materiel);
}


