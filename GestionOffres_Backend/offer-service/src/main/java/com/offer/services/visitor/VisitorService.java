package com.offer.services.visitor;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.offer.repository.AppelOffreRepository;
import com.offer.repository.CategorieRepository;
import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.projection.AppelOffreProjection;
@Service
public class VisitorService {
    @Autowired
    private AppelOffreRepository appelOffreRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
    public List<AppelOffreProjection> getAppelOffres() {
        // This now returns a List of AppelOffreProjection instances
        return appelOffreRepository.findAllWithLimitedFields();  
    }
    
	 
	
	    public List<AppelOffre> getAppelOffresByCategorieId(UUID categorieId) {
	        return appelOffreRepository.findByCategorieId(categorieId);
	    }
}
