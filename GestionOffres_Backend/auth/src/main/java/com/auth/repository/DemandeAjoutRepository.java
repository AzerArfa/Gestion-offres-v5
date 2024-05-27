package com.auth.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.entity.DemandeAjoutEntreprise;
@Repository
public interface DemandeAjoutRepository extends JpaRepository<DemandeAjoutEntreprise, UUID> {
	List<DemandeAjoutEntreprise> findByStatus(String status);
	List<DemandeAjoutEntreprise> findByUserId(UUID userId);

}
