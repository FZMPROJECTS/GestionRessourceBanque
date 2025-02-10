package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Entite;
import com.example.gestionressourcebanque.enums.StatutDemande;
import com.example.gestionressourcebanque.enums.TypeEntite;
import com.example.gestionressourcebanque.services.EntiteService;
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
public class EntiteController {

    @Autowired
    private EntiteService entiteService;

    @RequestMapping("/CreateEntite")
    public String createEntite(ModelMap modelMap) {
        modelMap.addAttribute("entite", new Entite());
        List<Entite> entites = entiteService.getAllEntites();
        modelMap.addAttribute("entites", entites);
        modelMap.addAttribute("typeEntiteOptions", TypeEntite.values());
        return "/pages/CreateEntite";
    }

    @RequestMapping("/saveEntite")
    public String saveEntite(@Valid @ModelAttribute("entite") Entite entite, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            List<Entite> entites = entiteService.getAllEntites();
            modelMap.addAttribute("entites", entites);
            modelMap.addAttribute("error", "Erreur lors de la création de l'entité");
            return "pages/CreateEntite";
        }
        try {
            entiteService.saveEntite(entite);
        } catch (Exception e) {
            modelMap.addAttribute("error", "Erreur lors de la création de l'entité");
            return "pages/CreateEntite";
        }
        return "redirect:/EntitesList";
    }

    @RequestMapping("/EntitesList")
    public String listEntites(ModelMap modelMap) {
        List<Entite> entites = entiteService.getAllEntites();
        modelMap.addAttribute("entites", entites);
        modelMap.addAttribute("entite", new Entite()); // Ajouter une nouvelle entité pour le formulaire du modal
        modelMap.addAttribute("typeEntiteOptions", TypeEntite.values());
        return "/pages/EntitesList";
    }

    @RequestMapping("/deleteEntite")
    public String deleteEntite(@RequestParam("id") long id, ModelMap modelMap) {
        try {
            entiteService.deleteEntiteById(id);
        } catch (Exception e) {
            modelMap.addAttribute("error", "Erreur lors de la suppression de l'entité");
            return "redirect:/EntitesList";
        }
        return "redirect:/EntitesList";
    }

    @RequestMapping("/EditEntite")
    public String editEntite(@RequestParam("id") long id, ModelMap modelMap) {
        Entite entite = entiteService.getEntiteById(id);
        modelMap.addAttribute("entite", entite);
        modelMap.addAttribute("typeEntiteOptions", TypeEntite.values());
        return "redirect:/EntitesList";
    }

    @RequestMapping("/updateEntite")
    public String updateEntite(@Valid @ModelAttribute("entite") Entite entite, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("error", "Erreur lors de la mise à jour de l'entité");
            return "redirect:/EntitesList";
        }
        try {
            entiteService.saveEntite(entite);
        } catch (Exception e) {
            modelMap.addAttribute("error", "Erreur lors de la mise à jour de l'entité");
            return "redirect:/EntitesList";
        }
        return "redirect:/EntitesList";
    }

    @RequestMapping("/updateEntiteStatut")
    public String updateEntiteStatut(@RequestParam("id") long id, ModelMap modelMap) {
        Entite entite = entiteService.getEntiteById(id);
        if (entite != null) {
            try {
                entiteService.saveEntite(entite);
            } catch (Exception e) {
                modelMap.addAttribute("error", "Erreur lors de la mise à jour du statut de l'entité");
                return "redirect:/EntitesList";
            }
        }
        return "redirect:/EntitesList";
    }
}