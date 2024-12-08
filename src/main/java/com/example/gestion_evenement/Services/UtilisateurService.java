package com.example.gestion_evenement.Services;

import com.example.gestion_evenement.Entites.Utilisateur;
import com.example.gestion_evenement.Repositories.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utilisateur enregistrerUtilisateur(Utilisateur utilisateur) {
        // Vérifier si un utilisateur avec cet email existe déjà
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Encoder le mot de passe avant l'enregistrement
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    // Récupérer tous les utilisateurs
    public List<Utilisateur> recupererTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}

