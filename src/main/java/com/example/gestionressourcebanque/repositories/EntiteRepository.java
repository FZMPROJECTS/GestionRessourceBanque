package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Entite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntiteRepository extends JpaRepository<Entite,Long> {
}
