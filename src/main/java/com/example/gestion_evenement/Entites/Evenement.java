package com.example.gestion_evenement.Entites;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.example.gestion_evenement.Enums.TypeEvenement;

@Entity
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrémenté
    private Long id;

    private String titre;

    private String description;

    @Temporal(TemporalType.DATE) // Spécifie le type de la colonne pour une date
    private LocalDate date;

    private String lieu;

    private int capacite;

    private BigDecimal prix; // Ajout du champ prix

    private Integer placesDisponibles;




    @Enumerated(EnumType.STRING) // Enum pour le type d'événement
    private TypeEvenement type;

    // Relation avec les participants (Utilisateurs)
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Utilisateur> utilisateurs;

    // Constructeur sans paramètres
    public Evenement() {}

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public TypeEvenement getType() {
        return type;
    }

    public void setType(TypeEvenement type) {
        this.type = type;
    }



    // Relation OneToMany avec Paiement
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL)
    private List<Paiement> paiements;
    // Relation avec les utilisateurs (participants)
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Integer getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(Integer placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
}


