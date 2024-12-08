package com.example.gestion_evenement.Repositories;

import com.example.gestion_evenement.Entites.Evenement;
import com.example.gestion_evenement.Entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findById(Long id);


}
