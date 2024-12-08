package com.example.gestion_evenement.Entites;

import com.example.gestion_evenement.Enums.StatutPaiement;
import jakarta.persistence.*;

@Entity
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    // L'identifiant unique de Stripe pour ce paiement
    @Column(nullable = false, unique = true)
    private String paymentIntentId;

    // Relation ManyToOne avec Utilisateur (un paiement est fait par un utilisateur)
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    // Relation ManyToOne avec Evenement (un paiement est pour un événement)
    @ManyToOne
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;

    // Constructeurs
    public Paiement() {}

    public Paiement(Double montant, StatutPaiement statut, String paymentIntentId, Utilisateur utilisateur, Evenement evenement) {
        this.montant = montant;
        this.statut = statut;
        this.paymentIntentId = paymentIntentId;
        this.utilisateur = utilisateur;
        this.evenement = evenement;
    }

    public Paiement(Double montant, StatutPaiement statutPaiement, Utilisateur utilisateur, Evenement evenement) {
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
}
