package com.example.gestion_evenement.Controller;


import com.example.gestion_evenement.Entites.Achat;
import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Repositories.AchatRepository;
import com.example.gestion_evenement.Repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/achat")
public class AchatController {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private AchatRepository achatRepository;

    @GetMapping("/acheter/{id}")
    public String afficherFormulaireAchat(@PathVariable("id") Long id, Model model) {
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        if (evenement == null) {
            model.addAttribute("message", "Événement non trouvé !");
            return "resultatAchat"; // Page d'erreur si l'événement n'est pas trouvé
        }

        model.addAttribute("evenement", evenement);
        return "acheter"; // Page d'achat
    }

    @PostMapping("/acheter/{id}")
    public String traiterAchat(@PathVariable("id") Long id, @RequestParam("quantite") int quantite, Model model) {
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        if (evenement == null || quantite <= 0) {
            model.addAttribute("message", "Événement non trouvé ou quantité invalide.");
            return "resultatAchat"; // Retourner à la page de confirmation avec message d'erreur
        }

        // Vérification des places disponibles
        if (evenement.getPlacesDisponibles() < quantite) {
            model.addAttribute("message", "Désolé, il n'y a pas assez de places disponibles.");
            return "resultatAchat"; // Message d'erreur si pas assez de places
        }

        // Calcul du prix total
        BigDecimal prixTotal = evenement.getPrix().multiply(BigDecimal.valueOf(quantite));

        // Créer une instance d'achat et enregistrer
        Achat achat = new Achat();
        achat.setEvenement(evenement);
        achat.setQuantite(quantite);
        achat.setPrixTotal(prixTotal);
        achatRepository.save(achat);

        // Mise à jour des places disponibles
        evenement.setPlacesDisponibles(evenement.getPlacesDisponibles() - quantite);
        evenementRepository.save(evenement);

        // Affichage du message de confirmation
        model.addAttribute("message", "Achat effectué avec succès ! Vous avez acheté " + quantite + " tickets pour " + evenement.getTitre());
        return "resultatAchat"; // Page de confirmation d'achat
    }
}

