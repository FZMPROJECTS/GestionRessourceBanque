package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Administrateur;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdministateurService {
    Administrateur saveAdministrateur(Administrateur administrateur);
    Administrateur updateAdministrateur(Administrateur administrateur);
    void deleteAdministrateurById(long id);
    void deleteAllAdministrateurs();
    Administrateur getAdministrateurById(long id);
    List<Administrateur> getAllAdministrateurs();
}
