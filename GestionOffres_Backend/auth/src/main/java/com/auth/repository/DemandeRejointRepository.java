package com.auth.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.DemandeRejoindreEntreprise;

public interface DemandeRejointRepository  extends JpaRepository<DemandeRejoindreEntreprise, UUID> {
	List<DemandeRejoindreEntreprise> findAllByEntrepriseMatriculeIn(List<String> entrepriseMatricules);
}
