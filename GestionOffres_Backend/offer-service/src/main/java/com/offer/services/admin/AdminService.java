package com.offer.services.admin;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.entity.Offre;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminService {

    AppelOffre createAppelOffre(AppelOffre appelOffre);

    List<AppelOffre> getAllAppelOffres();

    List<AppelOffre> getAppelOffresByEntrepriseId(UUID entrepriseId);
    void deleteAppelOffre(UUID id);

	Optional<AppelOffre> getAppelOffreById(UUID id);
	
	Offre createOffre(Offre offre);
	Offre updateOffre(Offre offre);
	void deleteOffre(UUID id);
	List<Offre> getAllOffres();
	Optional<Offre> getOffreById(UUID id);

	List<Offre> listAllOffresByAppelOffreId(UUID appelOffreId);

	List<Offre> getOffresByUserId(String userid);

	AppelOffre updateAppelOffre(UUID appelOffreId, AppelOffre updatedAppelOffre);

	List<Categorie> getAllCategories();

	List<AppelOffre> getAppelOffresByCategorieId(UUID categorieId);
}
