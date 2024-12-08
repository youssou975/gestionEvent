package com.example.gestion_evenement.Repositories;

import com.example.gestion_evenement.Entites.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    // Vous pouvez définir des méthodes supplémentaires si nécessaire
    // Exemple : Trouver les paiements par statut
        // Trouver les paiements par statut
        List<Paiement> findByStatut(String statut);

        // Trouver les paiements par événement
        List<Paiement> findByEvenementId(Long evenementId);

        // Trouver un paiement par son paymentIntentId
        Optional<Paiement> findByPaymentIntentId(String paymentIntentId);


}
