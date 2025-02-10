package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Entite;
import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.services.EntiteService;
import com.example.gestionressourcebanque.services.RoleService;
import com.example.gestionressourcebanque.services.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class UtilisateurController {

    @Autowired
    private final UtilisateurService utilisateurService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final EntiteService entiteService;

    @Autowired
    private final RoleService roleService;

    @RequestMapping("/CreateUtilisateur")
    public String createUtilisateur(ModelMap modelMap) {
        modelMap.addAttribute("utilisateur", new Utilisateur());

        List<Entite> entites = entiteService.getAllEntites();
        modelMap.addAttribute("entites", entites);

        List<Role> roles = roleService.getAllRoles();
        modelMap.addAttribute("roles", roles);



        return "/pages/CreateUtilisateur";
    }

    @PostMapping("/saveUtilisateur")
    public String saveUtilisateur(@ModelAttribute("utilisateur") Utilisateur utilisateur,
                                  ModelMap modelMap,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Encode the password
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));

            // Save the user with selected roles
            utilisateurService.saveUtilisateur(utilisateur);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", "Utilisateur enregistré avec succès.");

            // Redirect to the user list after successful save
            return "redirect:/UtilisateursList";
        } catch (Exception e) {
            // Log the exception (you should replace this with your preferred logging mechanism)
            e.printStackTrace();

            // Add error message for the user
            String errorMessage = "Une erreur est survenue lors de l'enregistrement de l'utilisateur : email ou username sont unique " ;
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

            // Redirect to an error page
            return "redirect:/CreateUtilisateur";
        }
    }

    @RequestMapping("/UtilisateursList")
    public String UtilisateurList(ModelMap modelMap) {
        List<Utilisateur> utilisateursController = utilisateurService.getAllUtilisateurs();
        modelMap.addAttribute("utilisateurs", utilisateursController);
        return "/pages/UtilisateursList";
    }

    @RequestMapping("/deleteUtilisateur")
    public String deleteUtilisateur(@RequestParam("id") long id, ModelMap modelMap) {
        utilisateurService.deleteUtilisateurById(id);
        List<Utilisateur> utilisateursController = utilisateurService.getAllUtilisateurs();
        modelMap.addAttribute("utilisateurs", utilisateursController);
        return "/pages/UtilisateursList";
    }

    @RequestMapping("/EditUtilisateur")
    public String editUtilisateur(@RequestParam("id") long id, ModelMap modelMap) {
        Utilisateur utilisateurController = utilisateurService.getUtilisateurById(id);
        modelMap.addAttribute("utilisateur", utilisateurController);
        return "/pages/EditUtilisateur";
    }

    @RequestMapping("/updateUtilisateur")
    public String updateUtilisateur(@ModelAttribute("utilisateur") Utilisateur utilisateurController, ModelMap modelMap) {
        utilisateurService.updateUtilisateur(utilisateurController);
        List<Utilisateur> utilisateursController = utilisateurService.getAllUtilisateurs();
        modelMap.addAttribute("utilisateurs", utilisateursController);
        return "/pages/UtilisateursList";
    }

    @RequestMapping("/UtilisateurDetails")
    public String utilisateurDetails(@RequestParam("id") long id, ModelMap modelMap) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        modelMap.addAttribute("utilisateur", utilisateur);
        return "redirect:/UtilisateursList";
    }

    @RequestMapping("/MonProfil")
    public String voirProfil(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByUsername(currentUsername);

        modelMap.addAttribute("utilisateur", utilisateurConnecte);

        return "pages/MonProfil";
    }

    @PostMapping("/updateProfile")
    public String updateProfil(@ModelAttribute("utilisateur") Utilisateur utilisateur,
                               @RequestParam(value = "oldPassword", required = false) String oldPassword,
                               @RequestParam(value = "newPassword", required = false) String newPassword,
                               @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                               ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByUsername(currentUsername);

        if (utilisateurConnecte != null) {
            // Mise à jour des champs
            utilisateurConnecte.setEmail(utilisateur.getEmail());
            utilisateurConnecte.setAddress(utilisateur.getAddress());
            utilisateurConnecte.setTelephone(utilisateur.getTelephone());

            // Gestion du changement de mot de passe
            if (newPassword != null && !newPassword.isEmpty()) {
                if (bCryptPasswordEncoder.matches(oldPassword, utilisateurConnecte.getPassword()) && newPassword.equals(confirmPassword)) {
                    utilisateurConnecte.setPassword(bCryptPasswordEncoder.encode(newPassword));
                } else {
                    modelMap.addAttribute("error", "Les mots de passe ne correspondent pas ou l'ancien mot de passe est incorrect.");
                    return "pages/MonProfil";
                }
            }

            try {
                utilisateurService.updateUtilisateur(utilisateurConnecte);
                modelMap.addAttribute("success", "Profil mis à jour avec succès.");
            } catch (Exception e) {
                modelMap.addAttribute("error", "Erreur lors de la mise à jour du profil: " + e.getMessage());
                return "pages/MonProfil";
            }
        } else {
            modelMap.addAttribute("error", "Utilisateur non trouvé.");
            return "pages/MonProfil";
        }

        return "redirect:/MonProfil";
    }
}
