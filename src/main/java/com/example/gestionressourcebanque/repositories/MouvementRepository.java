package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Mouvement d WHERE d.materiel.id = :materielId")
    void deleteByMaterielId(@Param("materielId") long materielId);

    Mouvement findByMateriel(Materiel materiel);
}
