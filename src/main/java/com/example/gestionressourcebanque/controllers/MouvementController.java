package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Mouvement;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.services.MaterielService;
import com.example.gestionressourcebanque.services.MouvementService;
import com.example.gestionressourcebanque.services.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MouvementController {
    @Autowired
    private MouvementService mouvementService;
    private MaterielService materielService;
    private UtilisateurService utilisateurService;

    @RequestMapping("/CreateMouvement")
    public String createMouvement(ModelMap modelMap) {
        modelMap.addAttribute("mouvement", new Mouvement());
        modelMap.addAttribute("materials", materielService.getMaterielsByStatutLibre());
        modelMap.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());

        return "/pages/CreateMouvement";
    }

    @RequestMapping("saveMouvement")
    public String saveMouvement(@Valid @ModelAttribute("mouvement") Mouvement mouvementController,
                                BindingResult bindingResult,
                                ModelMap modelMap,
                                @RequestParam("utilisateurId") Long utilisateurId,
                                @RequestParam("materielId") Long materielId) {
        if (bindingResult.hasErrors()) return "/pages/CreateMouvement";

        try {
            Materiel materiel=materielService.getMaterielById(materielId);
            materielService.updateSiteBconcerneFromUtilisateur(materielId, utilisateurId);
            if (materiel != null) {
            materiel.setStatut(StatutMateriel.Occupe);
                materielService.updateMateriel(materiel);
            }

        } catch (IllegalArgumentException e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
            return "/pages/CreateMouvement";
        }


        Materiel materiel = materielService.getMaterielById(materielId);
        mouvementController.setMateriel(materiel);

        mouvementService.saveMouvement(mouvementController);

        return "redirect:/CreateMouvement";
    }

    @RequestMapping("/MouvementsList")
    public String MouvementList(ModelMap modelMap) {
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        modelMap.addAttribute("mouvements", mouvements);
        return "/pages/MouvementsList";
    }

    @RequestMapping("/deleteMouvement")
    public String deleteMouvement(@RequestParam("id") long id, ModelMap modelMap) {
        mouvementService.deleteMouvementById(id);
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        modelMap.addAttribute("mouvements", mouvements);
        return "/pages/MouvementsList";
    }

    @RequestMapping("/EditMouvement")
    public String editMouvement(@RequestParam("id") long id, ModelMap modelMap) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        modelMap.addAttribute("mouvement", mouvement);
        return "/pages/EditMouvement";
    }

    @RequestMapping("/updateMouvement")
    public String updateMouvement(@ModelAttribute("mouvement") Mouvement mouvementController, ModelMap modelMap) {
        mouvementService.updateMouvement(mouvementController);
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        modelMap.addAttribute("mouvements", mouvements);
        return "/pages/MouvementsList";
    }


}
