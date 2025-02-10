package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface MaterielService {

    double getPercentageOfFreeMaterials();
    Materiel saveMateriel (Materiel materiel);
    void saveAllMateriels(List<Materiel> materiels) ;
    Materiel updateMateriel(Materiel materiel);
    void deleteMaterielById(long id);
    void deleteAllMateriels();
    Materiel getMaterielById(long id);
    Optional<Materiel> getMaterielById2(long id);

    List<Materiel> getAllMateriels();

    //ENREGISTREMENT MY TEST
    public  void saveTheMateriel(MultipartFile file , TypeMateriel type) throws IOException;

    Page<Materiel> getAllMateriels(Pageable pageable);

    //public Materiel updateSiteBConcerne(Long materielId, Long utilisateurId);

    public Materiel updateSiteBconcerneFromUtilisateur(Long materielId, Long utilisateurId);


    public List<Materiel> getMaterielsByType(TypeMateriel type);
    public Page<Materiel> getMaterielsByStatutLibre(Pageable pageable);
    public List<Materiel> getMaterielsByStatutLibre();




}