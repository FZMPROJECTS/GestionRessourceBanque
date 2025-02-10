package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByNom(String role);
}
