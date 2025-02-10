package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Correct import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterielRepository extends JpaRepository<Materiel,Long>  {
    List<Materiel> findByType(TypeMateriel type);



    Page<Materiel> findByStatut(StatutMateriel statut, Pageable pageable);
    List<Materiel> findByStatut(StatutMateriel statut);


}
