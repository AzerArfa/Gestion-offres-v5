package com.offer.controller.visitor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.projection.AppelOffreProjection; // Import the projection interface
import com.offer.services.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/offer/visitor")
@RequiredArgsConstructor
public class VisitorController {
    @Autowired
    private VisitorService visitorService;
  
    @GetMapping("/appeloffres")
    public List<AppelOffreProjection> getAppelOffres() { 
        // This method now returns a List of AppelOffreProjection
        return visitorService.getAppelOffres();
    }
    @GetMapping("/categories")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = visitorService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categorieId}/appeloffres")
    public ResponseEntity<List<AppelOffre>> getAppelOffresByCategorieId(@PathVariable UUID categorieId) {
        List<AppelOffre> appelOffres = visitorService.getAppelOffresByCategorieId(categorieId);
        if (appelOffres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appelOffres);
    }
}
