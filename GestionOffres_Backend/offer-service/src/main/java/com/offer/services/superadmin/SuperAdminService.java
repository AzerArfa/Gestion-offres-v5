package com.offer.services.superadmin;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.offer.entity.Categorie;
import com.offer.repository.AppelOffreRepository;
import com.offer.repository.CategorieRepository;
@Service
public class SuperAdminService {
    @Autowired
    private AppelOffreRepository appelOffreRepository;
	  @Autowired
	    private CategorieRepository categorieRepository;
	    public List<Categorie> getAllCategories() {
	        return categorieRepository.findAll();
	    }
	    public int getCountOfAppelOffreByCategorie(UUID categorieId) {
	        return appelOffreRepository.findByCategorieId(categorieId).size();
	    }
	    public Categorie addCategory(Categorie categorie) {
	        return categorieRepository.save(categorie);
	    }
	    public Categorie getCategoryById(UUID id) {
	        return categorieRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Category not found"));
	    }
	    public Categorie updateCategory(UUID id, Categorie newCategorieData) {
	        Categorie categorie = categorieRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Category not found"));
	        categorie.setNomcategorie(newCategorieData.getNomcategorie());
	        categorie.setDescription(newCategorieData.getDescription());
	        // Update other fields as necessary
	        return categorieRepository.save(categorie);
	    }
	    public void deleteCategory(UUID id) {
	        Categorie categorie = categorieRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Category not found"));
	        categorieRepository.delete(categorie);
	    }
	    public List<Categorie> searchCategoriesByNom(String nomCategorie) {
	        return categorieRepository.findByNomCategorieContainingIgnoreCase(nomCategorie);
	    }
}
