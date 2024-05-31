package com.auth.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.auth.entity.Notification;

import jakarta.transaction.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, UUID>{
	 @Query("SELECT n FROM Notification n WHERE n.id_categorie = :categorieId AND n.entreprise.id != :entrepriseId")
	    List<Notification> findByCategorieIdAndNotEntrepriseId(@Param("categorieId") UUID categorieId, @Param("entrepriseId") UUID entrepriseId);

	 @Query("SELECT n FROM Notification n WHERE n.id_appeloffre = :idAppelOffre")
	    List<Notification> findByIdAppeloffre(@Param("idAppelOffre") UUID idAppelOffre);
	 
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM Notification n WHERE n.id_appeloffre = :idAppelOffre")
	    void deleteByIdAppeloffre(@Param("idAppelOffre") UUID idAppelOffre);
	    
}
