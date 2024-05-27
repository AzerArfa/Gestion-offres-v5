package com.offer.repository;
import com.offer.entity.AppelOffre;
import com.offer.projection.AppelOffreProjection;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
@Repository
public interface AppelOffreRepository extends JpaRepository<AppelOffre, UUID> {
	List<AppelOffre> findByEntrepriseId(UUID entrepriseId);
	 @Query("SELECT a.titre as titre, a.datecreation as datecreation, a.datelimitesoumission as datelimitesoumission, a.localisation as localisation, a.img as img FROM AppelOffre a")
	    List<AppelOffreProjection> findAllWithLimitedFields();
	List<AppelOffre> findByCategorieId(UUID categorieId);

}
