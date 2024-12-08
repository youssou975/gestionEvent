package com.example.gestion_evenement.Controller;

import com.example.gestion_evenement.DTO.EvenementDTO;
import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Enums.TypeEvenement;
import com.example.gestion_evenement.Services.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/evenements")
public class EvenementController {

    private final EvenementService evenementService;

    @Autowired
    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    // Afficher la liste des événements dans le DashboardAdmin
    @GetMapping("/dashboard")
    public String afficherDashboard(Model model) {
        List<Evenement> evenements = evenementService.getTousLesEvenements();
        model.addAttribute("evenements", evenements);
        return "dashboardadmin"; // Vue pour afficher la liste des événements
    }

    // Afficher le formulaire pour ajouter un événement
    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("evenementDTO", new EvenementDTO());
        model.addAttribute("types", TypeEvenement.values());
        return "ajouter"; // Vue pour ajouter un événement
    }

    // Ajouter un nouvel événement
    @PostMapping("/ajouter")
    public String ajouterEvenement(@ModelAttribute("evenementDTO") EvenementDTO evenementDTO) {
        Evenement evenement = new Evenement();
        evenement.setTitre(evenementDTO.getTitre());
        evenement.setDescription(evenementDTO.getDescription());
        evenement.setDate(evenementDTO.getDate());
        evenement.setLieu(evenementDTO.getLieu());
        evenement.setCapacite(evenementDTO.getCapacite());
        evenement.setType(TypeEvenement.valueOf(evenementDTO.getType()));
        evenement.setPrix(evenementDTO.getPrix());
        evenementService.ajouterEvenement(evenement);
        return "redirect:/evenements/dashboard";
    }

    // Afficher le formulaire pour modifier un événement
    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        Evenement evenement = evenementService.getEvenementParId(id);
        if (evenement == null) {
            model.addAttribute("error", "L'événement avec l'ID " + id + " n'existe pas.");
            return "redirect:/evenements/dashboard";
        }
        model.addAttribute("evenement", evenement);
        model.addAttribute("types", TypeEvenement.values());
        return "modifierEvenement";
    }

    // Enregistrer les modifications d'un événement
    @PostMapping("/modifier")
    public String enregistrerModification(@ModelAttribute Evenement evenement) {
        evenementService.modifierEvenement(evenement);
        return "redirect:/evenements/dashboard";
    }

    // Supprimer un événement
    @GetMapping("/supprimer/{id}")
    public String supprimerEvenement(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return "redirect:/evenements/dashboard";
    }

    // Afficher la page d'accueil avec les événements
    @GetMapping("/accueil")
    public String afficherAccueil(Model model) {
        List<Evenement> evenements = evenementService.getTousLesEvenements();
        model.addAttribute("evenements", evenements);
        return "accueil";
    }

}
