package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.repositories.AdminstrateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdministrateurServiceImpl implements AdministateurService {

    private AdminstrateurRepository adminstrateurRepository;
    @Override
    public Administrateur saveAdministrateur(Administrateur administrateur) {
        return adminstrateurRepository.save(administrateur);
    }

    @Override
    public Administrateur updateAdministrateur(Administrateur administrateur) {
        return adminstrateurRepository.save(administrateur);
    }

    @Override
    public void deleteAdministrateurById(long id) {
        adminstrateurRepository.deleteById(id);
    }

    @Override
    public void deleteAllAdministrateurs() {
        adminstrateurRepository.deleteAll();

    }

    @Override
    public Administrateur getAdministrateurById(long id) {
        return adminstrateurRepository.findById(id).get();
    }

    @Override
    public List<Administrateur> getAllAdministrateurs() {
        return adminstrateurRepository.findAll();
    }
}
