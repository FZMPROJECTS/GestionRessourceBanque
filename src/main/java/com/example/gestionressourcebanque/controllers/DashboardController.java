package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Demande;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import com.example.gestionressourcebanque.services.DemandeService;
import com.example.gestionressourcebanque.services.MaterielService;
import com.example.gestionressourcebanque.services.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class DashboardController {
    @Autowired
    private UtilisateurService utilisateurService;
    private MaterielService materielService;
    private DemandeService demandeService;


    @RequestMapping("/")
    public String dashboard(ModelMap modelMap) {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        List<Materiel> materiels = materielService.getAllMateriels();

        List<Demande> demandesForToday = demandeService.getDemandesForToday();



        Map<TypeMateriel, Long> materielParType = new EnumMap<>(TypeMateriel.class);
        for (TypeMateriel type : TypeMateriel.values()) {
            materielParType.put(type, 0L);
        }
        for (Materiel materiel : materiels) {
            TypeMateriel type = materiel.getType();
            materielParType.put(type, materielParType.get(type) + 1);
        }

        modelMap.addAttribute("utilisateurs", utilisateurs);
        modelMap.addAttribute("materiels", materiels);


        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        modelMap.addAttribute("materielParType", materielParType);



        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        Map<String, Long> demandesByDate = demandeService.getDemandeCountsByDateRange(firstDayOfMonth, lastDayOfMonth);

        modelMap.addAttribute("demandesByDate", demandesByDate);

        modelMap.addAttribute("demandesForToday", demandesForToday);

        Long demandesThisMonth = demandeService.getDemandeCountForCurrentMonth();
        modelMap.addAttribute("demandesThisMonth", demandesThisMonth);



        double percentageOfFreeMaterials = materielService.getPercentageOfFreeMaterials();
        modelMap.addAttribute("percentageOfFreeMaterials", percentageOfFreeMaterials);


        // Compter le nombre total d'utilisateurs, d'administrateurs et d'utilisateurs réguliers
        long totalUtilisateurs = utilisateurService.getTotalUtilisateurs();
        double nombreAdministrateurs = utilisateurService.getPercentageAdministrateurs();
        double nombreUtilisateurs = utilisateurService.getPercentageUsers();

        // Stocker ces valeurs dans le modelMap pour les passer à la vue
        modelMap.addAttribute("totalUtilisateurs", totalUtilisateurs);
        modelMap.addAttribute("nombreAdministrateurs", nombreAdministrateurs);
        modelMap.addAttribute("nombreUtilisateurs", nombreUtilisateurs);

        return "/pages/dashboard";
    }
}
