package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Administrateur;
import com.example.gestionressourcebanque.services.AdministateurService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdministrateurController {
    @Autowired
    private AdministateurService administateurService;

    @RequestMapping("/CreateAdministrateur")
    public  String createAdministrateur(){
        return "CreateAdministrateur";
    }

    @RequestMapping("saveAdministrateur")
    public String saveAdministrateur(@Valid Administrateur AdministrateurController, BindingResult bindingResult, ModelMap modelMap){
        if (bindingResult.hasErrors()) return "CreateAdministrateur";

        Administrateur saveAdministrateur = administateurService.saveAdministrateur(AdministrateurController);
        return AdministrateurList(modelMap);

    }
    @RequestMapping("/AdministrateursList")
    public String AdministrateurList(ModelMap modelMap){
        List<Administrateur> AdministrateursController = administateurService.getAllAdministrateurs();
        modelMap.addAttribute("AdministrateursVue",AdministrateursController);
        return "AdministrateursList";

    }
    @RequestMapping("/deleteAdministrateur")
    public String deleteAdministrateur(@RequestParam("id") long id , ModelMap modelMap){
        administateurService.deleteAdministrateurById(id);
        List<Administrateur> AdministrateursController = administateurService.getAllAdministrateurs();
        modelMap.addAttribute("AdministrateursVue",AdministrateursController);
        return "AdministrateursList";

    }
    @RequestMapping("/EditAdministrateur")
    public String editAdministrateur(@RequestParam("id") long id ,ModelMap modelMap){
        Administrateur AdministrateurController = administateurService.getAdministrateurById(id);
        modelMap.addAttribute("AdministrateurView" , AdministrateurController);
        return "EditAdministrateur";


    }
    @RequestMapping("/updateAdministrateur")
    public String updateAdministrateur(@ModelAttribute("AdministrateurVue") Administrateur AdministrateurController, ModelMap modelMap){
        administateurService.updateAdministrateur(AdministrateurController);
        return AdministrateurList(modelMap);
    }
}
