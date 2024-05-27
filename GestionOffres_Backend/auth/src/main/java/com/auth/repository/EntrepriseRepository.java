package com.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.auth.entity.Entreprise;

public interface EntrepriseRepository extends JpaRepository<Entreprise, UUID>{
	    Optional<Entreprise> findByMatricule(String matricule);
}
