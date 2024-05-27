package com.offer.controller.superadmin;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.offer.entity.Categorie;
import com.offer.services.superadmin.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/offer/superadmin")
@RequiredArgsConstructor
public class SuperAdminController {
	private final SuperAdminService superadminService;
	
	 @GetMapping("/categories")
	    public ResponseEntity<List<Categorie>> getAllCategories() {
	        List<Categorie> categories = superadminService.getAllCategories();
	        if (categories.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(categories);
	    }
	 @GetMapping("/categories/{categorieId}/appeloffres/count")
	 public ResponseEntity<Integer> getAppelOffreCountByCategorie(@PathVariable UUID categorieId) {
	     int count = superadminService.getCountOfAppelOffreByCategorie(categorieId);
	     return ResponseEntity.ok(count);
	 }
	 @PostMapping("/categories")
	 public ResponseEntity<Categorie> addCategory(@RequestBody Categorie categorie) {
	     Categorie savedCategorie = superadminService.addCategory(categorie);
	     return ResponseEntity.created(URI.create("/offer/superadmin/categories/" + savedCategorie.getId())).body(savedCategorie);
	 }
	 @PutMapping("/categories/{id}")
	 public ResponseEntity<Categorie> updateCategory(@PathVariable UUID id, @RequestBody Categorie categorie) {
	     Categorie updatedCategorie = superadminService.updateCategory(id, categorie);
	     return ResponseEntity.ok(updatedCategorie);
	 }
	 @DeleteMapping("/categories/{id}")
	 public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
	     superadminService.deleteCategory(id);
	     return ResponseEntity.noContent().build();
	 }
	 @GetMapping("/categories/{id}")
	 public ResponseEntity<Categorie> getCategoryById(@PathVariable UUID id) {
	     try {
	         Categorie categorie = superadminService.getCategoryById(id);
	         return ResponseEntity.ok(categorie);
	     } catch (RuntimeException ex) {
	         return ResponseEntity.notFound().build();
	     }
	 }
	  @GetMapping("/search/{nomCategorie}")
	    public ResponseEntity<List<Categorie>> searchByNomCategorie(@PathVariable String nomCategorie) {
	        List<Categorie> categories = superadminService.searchCategoriesByNom(nomCategorie);
	        if (categories.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(categories);
	    }
}
