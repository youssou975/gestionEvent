package com.example.gestion_evenement.Repositories;


import com.example.gestion_evenement.Entites.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    @Override
    List<Evenement> findAll();

    Optional<Evenement> findById(Long id);
}

