package com.example.gestion_evenement.Services;

import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Entites.Paiement;
import com.example.gestion_evenement.Entites.Utilisateur;
import com.example.gestion_evenement.Enums.StatutPaiement;
import com.example.gestion_evenement.Repositories.EvenementRepository;
import com.example.gestion_evenement.Repositories.PaiementRepository;
import com.example.gestion_evenement.Repositories.UtilisateurRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EvenementRepository evenementRepository;

    @Autowired
    public PaiementService(PaiementRepository paiementRepository,
                           UtilisateurRepository utilisateurRepository,
                           EvenementRepository evenementRepository) {
        this.paiementRepository = paiementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.evenementRepository = evenementRepository;
    }

    // Créer un paiement avec Stripe et l'enregistrer dans la base de données
    public Paiement createPaiement(Double montant, String currency, Long utilisateurId, Long evenementId) throws StripeException {
        // Validation des entrées
        if (montant == null || montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif.");
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("La devise est obligatoire.");
        }

        // Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour l'ID : " + utilisateurId));

        // Récupérer l'événement
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new IllegalArgumentException("Événement introuvable pour l'ID : " + evenementId));

        // Créer un PaymentIntent via Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (montant * 100)) // Montant en centimes
                .setCurrency(currency)
                .setDescription("Paiement pour l'événement ID: " + evenementId)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        String clientSecret = paymentIntent.getClientSecret(); // Récupérer le client_secret

        // Créer l'objet Paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setStatut(StatutPaiement.Attente); // Par défaut : en attente
        paiement.setPaymentIntentId(clientSecret); // Stocker le client_secret pour la confirmation front-end
        paiement.setUtilisateur(utilisateur);
        paiement.setEvenement(evenement);

        // Enregistrer dans la base de données
        return paiementRepository.save(paiement);
    }


    // Mettre à jour le statut du paiement après confirmation
    public Paiement updatePaiementStatus(String paymentIntentId, StatutPaiement statut) {
        Optional<Paiement> optionalPaiement = paiementRepository.findByPaymentIntentId(paymentIntentId);
        if (optionalPaiement.isPresent()) {
            Paiement paiement = optionalPaiement.get();
            paiement.setStatut(statut);
            return paiementRepository.save(paiement);
        }
        throw new RuntimeException("Paiement introuvable pour l'ID Stripe : " + paymentIntentId);
    }
}
