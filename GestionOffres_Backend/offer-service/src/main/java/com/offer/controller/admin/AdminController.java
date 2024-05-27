package com.offer.controller.admin;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.entity.Offre;
import com.offer.repository.CategorieRepository;
import com.offer.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.offer.entity.Offre;
import com.offer.services.admin.AdminService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/offer/admin")
@RequiredArgsConstructor
public class AdminController {
	@Autowired
	private CategorieRepository categorieRepository;
    private final AdminService adminService;

    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> createAppelOffre(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("datelimitesoumission") @DateTimeFormat(pattern = "yyyy-MM-dd") Date datelimitesoumission,
            @RequestParam("entrepriseId") UUID entrepriseId,
            @RequestParam("localisation") String localisation,
            @RequestParam("document") MultipartFile document,
            @RequestParam("img") MultipartFile img,
            @RequestParam("categorieId") UUID categorieId) {

        Map<String, String> response = new HashMap<>();
        try {
            AppelOffre appelOffre = new AppelOffre();
            appelOffre.setTitre(titre);
            appelOffre.setDescription(description);
            appelOffre.setDatelimitesoumission(datelimitesoumission);
            appelOffre.setEntrepriseId(entrepriseId);
            appelOffre.setLocalisation(localisation);
            appelOffre.setCategorie(categorieRepository.findById(categorieId).orElse(null));  // Set the category

            if (img != null && !img.isEmpty()) {
                byte[] returnedImg = img.getBytes();
                appelOffre.setImg(returnedImg);
            }

            if (document != null && !document.isEmpty()) {
                if (!document.getContentType().equals("application/pdf")) {
                    response.put("error", "Document must be a PDF file.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                byte[] returnedDoc = document.getBytes();
                appelOffre.setDocument(returnedDoc);
            }

            AppelOffre createdOffer = adminService.createAppelOffre(appelOffre);
            if (createdOffer == null) {
                response.put("error", "Failed to create offer.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response.put("message", "Offer created successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error creating offer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = adminService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categorieId}/appeloffres")
    public ResponseEntity<List<AppelOffre>> getAppelOffresByCategorieId(@PathVariable UUID categorieId) {
        List<AppelOffre> appelOffres = adminService.getAppelOffresByCategorieId(categorieId);
        if (appelOffres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appelOffres);
    }
    @GetMapping
    public ResponseEntity<List<AppelOffre>> getAllAppelOffres() {
        return ResponseEntity.ok(adminService.getAllAppelOffres());
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateAppelOffre(
            @PathVariable UUID id,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String localisation,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date datelimitesoumission,
            @RequestParam(required = false) MultipartFile img,
            @RequestParam(required = false) MultipartFile document) {

        Map<String, String> response = new HashMap<>();
        try {
            Optional<AppelOffre> optionalOffer = adminService.getAppelOffreById(id);
            if (!optionalOffer.isPresent()) {
                response.put("error", "Offer not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            AppelOffre existingOffer = optionalOffer.get();
            if (titre != null) existingOffer.setTitre(titre);
            if (description != null) existingOffer.setDescription(description);
            if (datelimitesoumission != null) existingOffer.setDatelimitesoumission(datelimitesoumission);
            if (localisation != null) existingOffer.setLocalisation(localisation);
            if (img != null && !img.isEmpty()) {
                byte[] returnedImg = img.getBytes();
                existingOffer.setImg(returnedImg);
            }

            if (document != null && !document.isEmpty()) {
                if (!document.getContentType().equals("application/pdf")) {
                    response.put("error", "Document must be a PDF file.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                byte[] returnedDoc = document.getBytes();
                existingOffer.setDocument(returnedDoc);
            }

            AppelOffre updatedOffer = adminService.updateAppelOffre(id,existingOffer);
            if (updatedOffer == null) {
                response.put("error", "Failed to update offer.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response.put("message", "Offer updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error updating offer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppelOffre> getAppelOffreById(@PathVariable UUID id) {
        Optional<AppelOffre> appelOffre = adminService.getAppelOffreById(id);
        return appelOffre.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public void supprimerAppelOffre(@PathVariable UUID id) {
        adminService.deleteAppelOffre(id);
    }
    @GetMapping("/entreprises/{entrepriseId}/appeloffres")
    public ResponseEntity<List<AppelOffre>> getAppelOffresByEntrepriseId(@PathVariable UUID entrepriseId) {
        List<AppelOffre> appelOffres = adminService.getAppelOffresByEntrepriseId(entrepriseId);
        return ResponseEntity.ok(appelOffres);
    }
//GESTION OFFRE
    
    @PostMapping(value = "/offre/{appeloffreId}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> createOffre(
            @PathVariable("appeloffreId") UUID appeloffreId,  // Capture the ID from the URL
            @RequestParam("numtel") String numtel,
            @RequestParam("montant") double montant,
            @RequestParam("delaisderealisation") @DateTimeFormat(pattern = "yyyy-MM-dd") Date delaisderealisation,
            @RequestParam("entrepriseid") String entrepriseid,
          
            @RequestParam("documentdeproposition") MultipartFile documentdeproposition) {

        Map<String, String> response = new HashMap<>();
        try {
            Offre offre = new Offre();
            offre.setNumtel(numtel);
            offre.setMontant(montant);
            offre.setDelaisderealisation(delaisderealisation);
            offre.setEntrepriseid(entrepriseid);
    
            
            Optional<AppelOffre> optionalAppelOffre = adminService.getAppelOffreById(appeloffreId);
            if (optionalAppelOffre.isPresent()) {
                offre.setAppeloffre(optionalAppelOffre.get());
            } else {
                response.put("error", "AppelOffre with ID " + appeloffreId + " not found.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (documentdeproposition != null && !documentdeproposition.isEmpty()) {
                offre.setDocumentdeproposition(documentdeproposition.getBytes());
            }

            Offre createdOffre = adminService.createOffre(offre);
            response.put("message", "Offre created successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to create offre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/offres/admin/{userid}")
    public ResponseEntity<List<Offre>> getOffresByUserId(@PathVariable String userid) {
        List<Offre> offres = adminService.getOffresByUserId(userid);
        if (offres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(offres);
    }

    @PutMapping(value = "/offre/{id}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateOffre(
            @PathVariable UUID id,
            @RequestParam("numtel") String numtel,
            @RequestParam("montant") double montant,
            @RequestParam("delaisderealisation") @DateTimeFormat(pattern = "yyyy-MM-dd") Date delaisderealisation,
            @RequestParam("documentdeproposition") MultipartFile documentdeproposition) {

        Map<String, String> response = new HashMap<>();
        try {
            Optional<Offre> optionalOffre = adminService.getOffreById(id);
            if (!optionalOffre.isPresent()) {
                response.put("error", "Offre not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Offre existingOffre = optionalOffre.get();
            existingOffre.setNumtel(numtel);
            existingOffre.setMontant(montant);
            existingOffre.setDelaisderealisation(delaisderealisation);

            if (documentdeproposition != null && !documentdeproposition.isEmpty()) {
                existingOffre.setDocumentdeproposition(documentdeproposition.getBytes());
            }

            Offre updatedOffre = adminService.updateOffre(existingOffre);
            response.put("message", "Offre updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error updating offre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    
    @GetMapping("/offres")
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(adminService.getAllOffres());
    }

    @GetMapping("/offre/{id}")
    public ResponseEntity<?> getOffreById(@PathVariable UUID id) {
        Optional<Offre> offre = adminService.getOffreById(id);
        return offre.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/offre/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable UUID id) {
        try {
            adminService.deleteOffre(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete offre: " + e.getMessage());
        }
    }

    @GetMapping("/offres/appeloffre/{appeloffreId}")
    public ResponseEntity<List<Offre>> getOffresByAppelOffreId(@PathVariable("appeloffreId") UUID appeloffreId) {
        List<Offre> offres = adminService.listAllOffresByAppelOffreId(appeloffreId);
        return ResponseEntity.ok(offres);
    }
    
    
    @GetMapping("/offres/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID id) {
        Optional<Offre> offreOptional = adminService.getOffreById(id);
        if (offreOptional.isPresent()) {
            Offre offre = offreOptional.get();
            if (offre.getDocumentdeproposition() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id.toString() + ".pdf\"")
                        .body(offre.getDocumentdeproposition());
            } else {
                return ResponseEntity.noContent().build(); // No content to show that document is not available
            }
        } else {
            return ResponseEntity.notFound().build(); // Not found if the Offre itself does not exist
        }
    }
    @GetMapping("/appeloffres/download/{id}")
    public ResponseEntity<byte[]> downloadDocumentAppel(@PathVariable UUID id) {
        Optional<AppelOffre> offreOptional = adminService.getAppelOffreById(id);
        if (offreOptional.isPresent()) {
        	AppelOffre offre = offreOptional.get();
            if (offre.getDocument() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id.toString() + ".pdf\"")
                        .body(offre.getDocument());
            } else {
                return ResponseEntity.noContent().build(); 
            }
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
   
}
