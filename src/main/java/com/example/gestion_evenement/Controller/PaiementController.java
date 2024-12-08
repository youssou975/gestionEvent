package com.example.gestion_evenement.Controller;

import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Entites.Utilisateur;
import com.example.gestion_evenement.Security.UtilisateurDetails;
import com.example.gestion_evenement.Services.PaiementService;
import com.example.gestion_evenement.Entites.Paiement;
import com.example.gestion_evenement.Repositories.PaiementRepository;
import com.example.gestion_evenement.Repositories.UtilisateurRepository;
import com.example.gestion_evenement.Repositories.EvenementRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    @Value("${stripe.apiKey}") // Récupère la clé API Stripe depuis application.properties
    private String stripeApiKey;
    private final PaiementService paiementService;
    private final PaiementRepository paiementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EvenementRepository evenementRepository;

    public PaiementController(PaiementService paiementService, PaiementRepository paiementRepository,
                              UtilisateurRepository utilisateurRepository,
                              EvenementRepository evenementRepository) {
        this.paiementService = paiementService;
        this.paiementRepository = paiementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.evenementRepository = evenementRepository;
    }

    @GetMapping("/form")
    public String showPaiementForm(@RequestParam("evenementId") Long evenementId, Model model, Authentication authentication) {
        try {
            System.out.println("Authentication Principal : " + authentication.getPrincipal().getClass().getName());

            // Vérifier si le principal est une instance de UtilisateurDetails
            if (authentication.getPrincipal() instanceof UtilisateurDetails) {
                UtilisateurDetails utilisateurDetails = (UtilisateurDetails) authentication.getPrincipal();
                Utilisateur utilisateurConnecte = utilisateurDetails.getUtilisateur();

                System.out.println("Utilisateur connecté : " + utilisateurConnecte);
                model.addAttribute("utilisateurConnecte", utilisateurConnecte);
            } else {
                throw new IllegalArgumentException("L'utilisateur n'est pas correctement authentifié.");
            }

            Evenement evenement = evenementRepository.findById(evenementId)
                    .orElseThrow(() -> new IllegalArgumentException("Événement non trouvé : " + evenementId));

            System.out.println("Événement récupéré : " + evenement);

            model.addAttribute("evenementSelectionne", evenement);
            model.addAttribute("paiement", new Paiement());

            return "paiement";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createPaiement(@RequestBody Map<String, Object> requestData, Authentication authentication) {
        try {
            System.out.println("Données reçues pour le paiement : " + requestData);

            if (!requestData.containsKey("montant") || !requestData.containsKey("evenementId")) {
                throw new IllegalArgumentException("Les champs montant et evenementId sont obligatoires.");
            }

            Double montant = Double.parseDouble(requestData.get("montant").toString());
            Long evenementId = Long.parseLong(requestData.get("evenementId").toString());

            // Vérifier l'utilisateur connecté
            if (authentication.getPrincipal() instanceof UtilisateurDetails) {
                UtilisateurDetails utilisateurDetails = (UtilisateurDetails) authentication.getPrincipal();
                Utilisateur utilisateurConnecte = utilisateurDetails.getUtilisateur();

                Paiement paiement = paiementService.createPaiement(montant, "eur", utilisateurConnecte.getId(), evenementId);

                return ResponseEntity.ok(Map.of(
                        "clientSecret", paiement.getPaymentIntentId(),
                        "message", "Paiement créé avec succès."
                ));
            }

            throw new IllegalStateException("Utilisateur non valide.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la création du paiement."));
        }
    }

    @GetMapping("/liste-paiements")
    public String afficherListePaiements(Model model) {
        List<Paiement> paiements = paiementService.getAllPaiements();
        model.addAttribute("paiements", paiements);
        return "liste_paiements"; // Nom de la page Thymeleaf
    }

}
