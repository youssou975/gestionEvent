package com.example.gestion_evenement.Repositories;

import com.example.gestion_evenement.Entites.Achat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchatRepository extends JpaRepository<Achat, Long> {
}

