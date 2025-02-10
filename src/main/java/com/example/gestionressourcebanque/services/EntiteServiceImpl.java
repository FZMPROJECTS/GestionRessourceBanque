package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Entite;
import com.example.gestionressourcebanque.repositories.EntiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EntiteServiceImpl implements EntiteService{
    private EntiteRepository EntiteRepository ;
    @Override
    public Entite saveEntite(Entite entite) {
        return EntiteRepository.save(entite);
    }

    @Override
    public Entite updateEntite(Entite entite) {
        return EntiteRepository.save(entite);
    }

    @Override
    public void deleteEntiteById(long id) {
        EntiteRepository.deleteById(id);
    }

    @Override
    public void deleteAllEntites() {
        EntiteRepository.deleteAll();
    }

    @Override
    public Entite getEntiteById(long id) {
        return EntiteRepository.findById(id).get();
    }

    @Override
    public List<Entite> getAllEntites() {
        return EntiteRepository.findAll();
    }


}
