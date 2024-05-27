package com.offer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.offer.entity.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
	 @Query("SELECT c FROM Categorie c WHERE LOWER(c.nomcategorie) LIKE LOWER(CONCAT('%', :nomCategorie, '%'))")
	    List<Categorie> findByNomCategorieContainingIgnoreCase(String nomCategorie);
}
