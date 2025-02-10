package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.*;
import com.example.gestionressourcebanque.enums.StatutDemande;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.services.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.gestionressourcebanque.enums.StatutDemande.En_Cours;

@Controller
@AllArgsConstructor
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    private UtilisateurService utilisateurService;
    private MaterielService materielService;
    private NotificationService notificationService;


    @RequestMapping("/CreateDemande")
    public String createDemande(ModelMap modelMap) {
        Demande newDemande = new Demande();

        modelMap.addAttribute("demande", newDemande);


        //modelMap.addAttribute("statutDemandeOptions", StatutDemande.values());

        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        modelMap.addAttribute("utilisateurs", utilisateurs);

        List<Materiel> materiels = materielService.getMaterielsByStatutLibre();
        modelMap.addAttribute("materiels", materiels);

        return "/pages/CreateDemande";
    }

    @RequestMapping("/saveDemande")
    public String saveDemande(@Valid @ModelAttribute("demande") Demande demande, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            // Recharger les listes de données pour le formulaire
            List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
            modelMap.addAttribute("utilisateurs", utilisateurs);

            List<Materiel> materiels = materielService.getMaterielsByStatutLibre();
            modelMap.addAttribute("materiels", materiels);


            return "redirect:/CreateDemande";  // Retourner à la page de création avec les erreurs
        }

        try {
            demande.setDateDemande(LocalDate.now());
            demande.setStatut(En_Cours);  // Assurez-vous que le statut est correctement référencé
            demandeService.saveDemande(demande);
            // Mettre à jour le statut du matériel à "Occupé"
            Materiel materiel = demande.getMateriel();  // Assurez-vous que la demande contient le matériel associé
            if (materiel != null) {
                materiel.setStatut(StatutMateriel.Occupe);
                materielService.updateMateriel(materiel);  // Mettre à jour le statut du matériel
            } else {
                modelMap.addAttribute("error", "Matériel non trouvé ou non associé à la demande.");
                return "redirect:CreateDemande";
            }

        } catch (DataIntegrityViolationException e) {
            modelMap.addAttribute("error", "Une demande a déjà été faite pour ce matériel.");

            // Recharger les listes de données pour le formulaire
            List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
            modelMap.addAttribute("utilisateurs", utilisateurs);

            List<Materiel> materiels = materielService.getAllMateriels();
            modelMap.addAttribute("materiels", materiels);

            return "/pages/CreateDemande";  // Retourner à la page de création avec le message d'erreur
        }

        return "redirect:/CreateDemande";
    }

    @RequestMapping("/DemandesList")
    public String DemandeList(@RequestParam(required = false, defaultValue = "EN_COURS") String type, ModelMap modelMap) {
        System.out.println("Début de la méthode DemandeList avec type: " + type);

        // Récupérer le nom d'utilisateur de l'utilisateur connecté
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println("Nom d'utilisateur connecté: " + username);

        // Récupérer les rôles de l'utilisateur connecté
        boolean isUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_USER") ||
                        grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_UTILISATEUR")
                );
        System.out.println("L'utilisateur a le rôle 'Utilisateur': " + isUserRole);

        List<Demande> demandes;

        if (isUserRole) {
            System.out.println("Traitement pour un utilisateur avec le rôle 'Utilisateur'");
            Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username);
            System.out.println("ID de l'utilisateur: " + utilisateur.getId());

            if ("CONFIRMER".equalsIgnoreCase(type)) {
                System.out.println("Recherche des demandes confirmées pour l'utilisateur");
                demandes = demandeService.getDemandesByUtilisateurAndStatut(utilisateur.getId(), StatutDemande.Confirmer);
            } else {
                System.out.println("Recherche des demandes en cours pour l'utilisateur");
                demandes = demandeService.getDemandesByUtilisateurAndStatut(utilisateur.getId(), StatutDemande.En_Cours);
            }
        } else {
            System.out.println("Traitement pour un utilisateur sans le rôle 'Utilisateur'");
            if ("CONFIRMER".equalsIgnoreCase(type)) {
                System.out.println("Recherche de toutes les demandes confirmées");
                demandes = demandeService.getDemandesConfirmer();
            } else {
                System.out.println("Recherche de toutes les demandes en cours");
                demandes = demandeService.getDemandesEnCours();
            }
        }

        System.out.println("Nombre de demandes trouvées: " + demandes.size());

        modelMap.addAttribute("demandes", demandes);
        modelMap.addAttribute("statutDemandeOptions", StatutDemande.values());
        modelMap.addAttribute("type", type);

        System.out.println("Fin de la méthode DemandeList, retour à la vue 'pages/DemandesList'");
        return "pages/DemandesList";
    }


    @RequestMapping("/deleteDemande")
    public String deleteDemande(@RequestParam("id") long id, ModelMap modelMap) {
        demandeService.deleteDemandeById(id);
        return "redirect:/DemandesList";
    }

    @RequestMapping("/EditDemande")
    public String editDemande(@RequestParam("id") long id, ModelMap modelMap) {
        Demande demande = demandeService.getDemandeById(id);
        modelMap.addAttribute("demande", demande);
        modelMap.addAttribute("statutDemandeOptions", StatutDemande.values());
        return "/pages/EditDemande";
    }

    @RequestMapping("/updateDemande")
    public String updateDemande(@ModelAttribute("demande") Demande demande, ModelMap modelMap) {
        demandeService.saveDemande(demande);
        return "redirect:/DemandesList";
    }

    @RequestMapping("/updateDemandeStatut")
    public String updateDemandeStatut(@RequestParam("id") long id,
                                      @RequestParam("statut") StatutDemande statut,
                                      ModelMap modelMap) {
        Demande demande = demandeService.getDemandeById(id);
        if (demande != null) {
            demande.setStatut(statut);
            demandeService.saveDemande(demande);

            if (statut == StatutDemande.Rejeter) {
                Materiel materiel = demande.getMateriel();
                materiel.setStatut(StatutMateriel.Libre);
                materielService.saveMateriel(materiel);

                // Ajouter une notification pour informer l'utilisateur du rejet de sa demande
                Utilisateur utilisateur = demande.getUtilisateur();
                notificationService.addNotification(
                        "Votre demande pour le matériel " + materiel.getType() + " a été rejetée.",
                        utilisateur.getId()
                );
                demandeService.deleteDemandeById(id);
            }

        }
        return "redirect:/DemandesList";
    }



    @GetMapping("/DemanderMateriel")
    public String demanderMateriel(@RequestParam("id") long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Materiel materiel = materielService.getMaterielById(id);

        // Récupérer le nom d'utilisateur de l'utilisateur connecté
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Récupérer l'utilisateur connecté via son nom d'utilisateur
        Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username);

        if (materiel != null) {
            if (materiel.getStatut() == StatutMateriel.Libre) {
                // Créer une nouvelle demande
                Demande demande = new Demande();
                demande.setMateriel(materiel);
                demande.setStatut(StatutDemande.En_Cours);
                demande.setDateDemande(LocalDate.now());
                demande.setUtilisateur(utilisateur);

                // Changer le statut du matériel à Occupe
                materiel.setStatut(StatutMateriel.Occupe);
                materielService.updateMateriel(materiel);

                demandeService.saveDemande(demande);

                redirectAttributes.addFlashAttribute("message", "Demande créée avec succès pour le matériel " + materiel.getType() + " (numéro de série: " + materiel.getNum_serie() + ")");
            } else {
                redirectAttributes.addFlashAttribute("error", "Le matériel " + materiel.getType() + " (numéro de série: " + materiel.getNum_serie() + ") est déjà occupé.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Matériel non trouvé");
        }
        return "redirect:/MaterielsList";
    }

    //Pour Notifier
    @GetMapping("/notifications")
    public String getNotifications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username);

        if (utilisateur != null) {
            List<Notification> notifications = notificationService.getNotifications(utilisateur.getId());
            model.addAttribute("userNotifications", notifications);
        }

        return "pages/notifications";
    }

    @PostMapping("/markAsRead")
    public String markAsRead(@RequestParam("id") long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }


    @ModelAttribute("userNotifications")
    public List<Notification> getUserNotifications() {
        System.out.println("Début de getUserNotifications");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Est authentifié: " + auth.isAuthenticated());

        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println("Username récupéré: " + username);

        Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username);
        System.out.println("Utilisateur trouvé: " + (utilisateur != null ? utilisateur.getUsername() : "null"));

        if (utilisateur != null) {
            System.out.println("ID de l'utilisateur: " + utilisateur.getId());
            List<Notification> notifications = notificationService.getNotifications(utilisateur.getId());
            System.out.println("Notifications pour l'utilisateur " + utilisateur.getUsername() + ": " + notifications.size());
            return notifications != null ? notifications : new ArrayList<>();
        } else {
            System.out.println("Aucun utilisateur trouvé, retour d'une liste vide");
            return new ArrayList<>();
        }
    }
}
