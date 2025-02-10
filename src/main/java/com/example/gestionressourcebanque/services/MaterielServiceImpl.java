package com.example.gestionressourcebanque.services;

import ch.qos.logback.core.util.StringUtil;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import com.example.gestionressourcebanque.repositories.DemandeRepository;
import com.example.gestionressourcebanque.repositories.MaterielRepository;
import com.example.gestionressourcebanque.repositories.MouvementRepository;
import com.example.gestionressourcebanque.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class MaterielServiceImpl implements MaterielService{
    private MaterielRepository materielRepository ;
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private DemandeRepository demandeRepository; // Assurez-vous d'avoir un repository pour Demande
    @Autowired
    private MouvementRepository mouvementRepository; // Assurez-vous d'avoir un repository pour Demande

    // Méthode pour convertir LocalDateTime en Date
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    @Override
    public double getPercentageOfFreeMaterials() {
        List<Materiel> allMaterials = materielRepository.findAll();
        long totalMaterials = allMaterials.size();

        if (totalMaterials == 0) {
            return 0.0; // Éviter la division par zéro
        }

        long freeMaterials = allMaterials.stream()
                .filter(materiel -> materiel.getStatut() == StatutMateriel.Libre)
                .count();

        return (double) freeMaterials / totalMaterials * 100;
    }
    @Override
    public Materiel saveMateriel(Materiel materiel) {
        return materielRepository.save(materiel);
    }

    @Override
    public Materiel updateMateriel(Materiel materiel) {
        return materielRepository.save(materiel);
    }

    //@Override
    //public void deleteMaterielById(long id) {
    //    materielRepository.deleteById(id);
   // }

    @Override
    public void deleteAllMateriels() {
        materielRepository.deleteAll();
    }

    @Override
    public Materiel getMaterielById(long id) {
        return materielRepository.findById(id).get();
    }
    @Override
    public Optional<Materiel> getMaterielById2(long id) {
        return materielRepository.findById(id);
    }

    @Override
    public List<Materiel> getAllMateriels() {
        return materielRepository.findAll();
    }


    public void saveAllMateriels(List<Materiel> materiels) {
        materielRepository.saveAll(materiels);
    }

    public void saveAllMateriels(List<MultipartFile> files, List<TypeMateriel> types) throws IOException {
        if (files.size() != types.size()) {
            throw new IllegalArgumentException("Files and types lists must have the same size");
        }

        List<Materiel> materiels = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            TypeMateriel type = types.get(i);

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                System.out.println("Not a valid file");
                continue;
            }

            Materiel m = new Materiel();
            m.setImage(Base64.getEncoder().encodeToString(file.getBytes()).getBytes());
            m.setType(type);
            materiels.add(m);
        }

        materielRepository.saveAll(materiels);
    }


    //ENREGISTREMENT MY TEST
    @Override
    public void saveTheMateriel(MultipartFile file, TypeMateriel type) throws IOException {
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        if(fileName.contains(".."))
        {
            System.out.println("Not Valid file");
        }
        Materiel m=new Materiel();
        m.setImage(Base64.getEncoder().encodeToString(file.getBytes()).getBytes());
        m.setType(type);
        materielRepository.save(m);
    }

    @Override
    public Page<Materiel> getAllMateriels(Pageable pageable) {
        return materielRepository.findAll(pageable);
    }


    @Override
    public Materiel updateSiteBconcerneFromUtilisateur(Long materielId, Long utilisateurId) {
        // Récupérer l'utilisateur à partir de son ID
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        if (utilisateurOptional.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur avec l'ID " + utilisateurId + " n'existe pas.");
        }

        Utilisateur utilisateur = utilisateurOptional.get();

        // Récupérer le matériel à partir de son ID
        Optional<Materiel> materielOptional = materielRepository.findById(materielId);
        if (materielOptional.isEmpty()) {
            throw new IllegalArgumentException("Materiel avec l'ID " + materielId + " n'existe pas.");
        }

        Materiel materiel = materielOptional.get();

        // Stocker l'ancienne valeur de site_B_concerne dans site_A_concerne
        materiel.setSite_A_concerne(materiel.getSite_B_concerne());

        // Mettre à jour le champ siteBconcerne à partir de l'utilisateur
        materiel.setSite_B_concerne(utilisateur.getEntite().getNom());

        materiel.setStatut(StatutMateriel.Occupe);

        materiel.setDate_evenement_A(materiel.getDate_evenement_B());

        materiel.setDate_evenement_B(toDate(LocalDateTime.now()));

        // Sauvegarder et retourner le matériel mis à jour
        return materielRepository.save(materiel);
    }


    @Override
    public List<Materiel> getMaterielsByType(TypeMateriel type) {
        return materielRepository.findByType(type);
    }

    @Override
    public Page<Materiel> getMaterielsByStatutLibre(Pageable pageable) {
        return materielRepository.findByStatut(StatutMateriel.Libre, pageable);
    }

    @Override
    public List<Materiel> getMaterielsByStatutLibre() {
        return materielRepository.findByStatut(StatutMateriel.Libre);
    }


    @Override
    @Transactional
    public void deleteMaterielById(long id) {
        if (materielRepository.existsById(id)) {
            // Supprimer les demandes associées
            demandeRepository.deleteByMaterielId(id);
            mouvementRepository.deleteByMaterielId(id);

            // Supprimer le matériel
            materielRepository.deleteById(id);
        } else {
            throw new RuntimeException("Le matériel avec l'ID " + id + " n'existe pas.");
        }
    }
}