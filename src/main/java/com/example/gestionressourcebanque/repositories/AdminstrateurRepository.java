package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminstrateurRepository extends JpaRepository< Administrateur,Long> {

}
