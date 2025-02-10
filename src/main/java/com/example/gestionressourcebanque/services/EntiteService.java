package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Entite;
import com.example.gestionressourcebanque.entities.Entite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EntiteService {
    Entite saveEntite (Entite entite);
    Entite updateEntite(Entite entite);
    void deleteEntiteById(long id);
    void deleteAllEntites();
    Entite getEntiteById(long id);
    List<Entite> getAllEntites();
}
