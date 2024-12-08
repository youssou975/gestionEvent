package com.example.gestion_evenement.Controller;
import com.example.gestion_evenement.Services.PaiementService;
import com.example.gestion_evenement.Entites.Paiement;
import com.example.gestion_evenement.Repositories.PaiementRepository;
import com.example.gestion_evenement.Repositories.UtilisateurRepository;
import com.example.gestion_evenement.Repositories.EvenementRepository;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Affiche la page de paiement.
     */
    @GetMapping("/form")
    public String showPaiementForm(Model model) {


        model.addAttribute("utilisateurs", utilisateurRepository.findAll());
        model.addAttribute("evenements", evenementRepository.findAll());

        // Ajouter les données nécessaires au modèle
        model.addAttribute("paiement", new Paiement());
        return "paiement"; // Redirige vers paiement.html
    }

    /**
     * Crée un paiement avec Stripe.
     */
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createPaiement(@RequestBody Map<String, Object> requestData) {
        try {
            // Validation des données reçues
            if (!requestData.containsKey("montant") || !requestData.containsKey("utilisateurId") || !requestData.containsKey("evenementId")) {
                throw new IllegalArgumentException("Les champs montant, utilisateurId et evenementId sont obligatoires.");
            }

            // Extraire les données
            Double montant = Double.parseDouble(requestData.get("montant").toString());
            Long utilisateurId = Long.parseLong(requestData.get("utilisateurId").toString());
            Long evenementId = Long.parseLong(requestData.get("evenementId").toString());

            // Appeler le service pour créer le paiement
            Paiement paiement = paiementService.createPaiement(montant, "eur", utilisateurId, evenementId);

            // Retourner le client_secret pour le front-end
            return ResponseEntity.ok(Map.of(
                    "clientSecret", paiement.getPaymentIntentId(),
                    "message", "Paiement créé avec succès."
            ));

        } catch (IllegalArgumentException e) {
            // Erreur de validation des données
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            // Gestion des autres erreurs
            e.printStackTrace(); // Log du détail pour le débogage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur est survenue lors de la création du paiement."));
        }
    }

}
