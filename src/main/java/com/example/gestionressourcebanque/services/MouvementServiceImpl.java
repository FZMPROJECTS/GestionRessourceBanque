package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Mouvement;
import com.example.gestionressourcebanque.enums.EtatMateriel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import com.example.gestionressourcebanque.repositories.MaterielRepository;
import com.example.gestionressourcebanque.repositories.MouvementRepository;
import com.example.gestionressourcebanque.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MouvementServiceImpl implements MouvementService{
    private MouvementRepository mouvementRepository;
    private UserRepository userRepository;
    private MaterielRepository materielRepository;
    @Override
    public Mouvement saveMouvement(Mouvement mouvement) {
        return mouvementRepository.save(mouvement);
    }

    @Override
    public Mouvement updateMouvement(Mouvement mouvement) {
        return mouvementRepository.save(mouvement);
    }

    @Override
    public void deleteMouvementById(long id) {
        mouvementRepository.deleteById(id);
    }

    @Override
    public void deleteAllMouvements() {
        mouvementRepository.deleteAll();
    }

    @Override
    public Mouvement getMouvementById(long id) {
        return mouvementRepository.findById(id).get();
    }

    @Override
    public List<Mouvement> getAllMouvements() {
        return mouvementRepository.findAll();
    }

    @Override
    public Mouvement getMateriel(Materiel materiel) {
        return mouvementRepository.findByMateriel(materiel);
    }


}
