package com.example.gestion_evenement.Controller;

import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Enums.Role;
import com.example.gestion_evenement.Entites.Utilisateur;
import com.example.gestion_evenement.Services.UtilisateurService;
import com.example.gestion_evenement.Services.EvenementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final EvenementService evenementService;

    public UtilisateurController(UtilisateurService utilisateurService, EvenementService evenementService) {
        this.utilisateurService = utilisateurService;
        this.evenementService = evenementService;
    }

    @GetMapping("/register")
    public String afficherFormulaireInscription(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping("/register")
    public String enregistrerUtilisateur(@ModelAttribute Utilisateur utilisateur,
                                         RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.enregistrerUtilisateur(utilisateur);
            redirectAttributes.addFlashAttribute("message", "Inscription réussie. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }



    @GetMapping("/login")
    public String afficherPageLogin() {
        return "login";
    }

    @GetMapping("/home")
   public String afficherAccueil(Model model) {
        List<Evenement> evenements =  evenementService.getTousLesEvenements();
        model.addAttribute("evenements", evenements);
    return "/home";
    }

    // Afficher la liste des utilisateurs
    @GetMapping("/utilisateurs")
    public String afficherListeUtilisateurs(Model model) {
        // Récupérer tous les utilisateurs depuis le service
        List<Utilisateur> utilisateurs = utilisateurService.recupererTousLesUtilisateurs();

        // Ajouter les utilisateurs au modèle pour les passer à la vue
        model.addAttribute("utilisateurs", utilisateurs);

        return "liste_utilisateurs"; // Nom du fichier HTML
    }



    // Supprimer un utilisateur
    @GetMapping("/utilisateur/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.supprimerUtilisateur(id);
            redirectAttributes.addFlashAttribute("message", "Utilisateur supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression de l'utilisateur.");
        }
        return "redirect:/utilisateurs";
    }

}

