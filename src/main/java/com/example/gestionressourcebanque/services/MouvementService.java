package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Mouvement;
import com.example.gestionressourcebanque.entities.Mouvement;
import com.example.gestionressourcebanque.enums.EtatMateriel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface MouvementService {
    Mouvement saveMouvement (Mouvement mouvement);
    Mouvement updateMouvement(Mouvement mouvement);
    void deleteMouvementById(long id);
    void deleteAllMouvements();
    Mouvement getMouvementById(long id);
    List<Mouvement> getAllMouvements();


    Mouvement getMateriel(Materiel materiel);
}
