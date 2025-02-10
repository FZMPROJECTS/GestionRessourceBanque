package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    // Méthode pour obtenir le nombre total d'utilisateurs
    @Query("SELECT COUNT(u) FROM Utilisateur u")
    long countTotalUtilisateurs();

    // Méthode pour obtenir le nombre d'utilisateurs avec un rôle spécifique
    @Query("SELECT COUNT(u) FROM Utilisateur u JOIN u.roles r WHERE r.nom = :roleName")
    long countUtilisateursByRole(String roleName);

    Utilisateur findByUsername(String username);



    @Query("SELECT u FROM Utilisateur u JOIN u.roles r WHERE r.nom IN :roleNames")
    List<Utilisateur> findByRoles_NameIn(@Param("roleNames") List<String> roleNames);

}
