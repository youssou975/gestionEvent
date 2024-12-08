package com.example.gestion_evenement.Entites;

import com.example.gestion_evenement.Enums.Role;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Relation OneToMany avec Paiement
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Paiement> paiements;

    @ManyToOne
    @JoinColumn(name = "evenement_id") // La colonne de la clé étrangère
    private Evenement evenement;


    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String email, String password, Role role,Evenement evenement) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.evenement = evenement;

    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
}
