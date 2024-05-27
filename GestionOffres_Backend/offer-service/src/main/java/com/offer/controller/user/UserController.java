package com.offer.controller.user;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.entity.Offre;
import com.offer.services.user.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/offer/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<AppelOffre> getOfferById(@PathVariable UUID id) {
        Optional<AppelOffre> appelOffre = userService.getAppelOffreById(id);
        return appelOffre.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<AppelOffre>> getAllOffers(){
        List<AppelOffre> appelOffres = userService.getAllAppelOffres();
        return ResponseEntity.ok(appelOffres);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = userService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categorieId}/appeloffres")
    public ResponseEntity<List<AppelOffre>> getAppelOffresByCategorieId(@PathVariable UUID categorieId) {
        List<AppelOffre> appelOffres = userService.getAppelOffresByCategorieId(categorieId);
        if (appelOffres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appelOffres);
    }
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
            // Fetch the AppelOffre entity using the ID from the path variable
            Optional<AppelOffre> optionalAppelOffre = userService.getAppelOffreById(appeloffreId);
            if (optionalAppelOffre.isPresent()) {
                offre.setAppeloffre(optionalAppelOffre.get());
            } else {
                response.put("error", "AppelOffre with ID " + appeloffreId + " not found.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (documentdeproposition != null && !documentdeproposition.isEmpty()) {
                offre.setDocumentdeproposition(documentdeproposition.getBytes());
            }

            Offre createdOffre = userService.createOffre(offre);
            response.put("message", "Offre created successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to create offre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/offres/user/{userid}")
    public ResponseEntity<List<Offre>> getOffresByUserId(@PathVariable String userid) {
        List<Offre> offres = userService.getOffresByUserId(userid);
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
            Optional<Offre> optionalOffre = userService.getOffreById(id);
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

            Offre updatedOffre = userService.updateOffre(existingOffre);
            response.put("message", "Offre updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error updating offre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    
    @GetMapping("/offres")
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(userService.getAllOffres());
    }

    @GetMapping("/offre/{id}")
    public ResponseEntity<?> getOffreById(@PathVariable UUID id) {
        Optional<Offre> offre = userService.getOffreById(id);
        return offre.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/offre/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable UUID id) {
        try {
        	userService.deleteOffre(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete offre: " + e.getMessage());
        }
    }
    @GetMapping("/offres/appeloffre/{appeloffreId}")
    public ResponseEntity<List<Offre>> getOffresByAppelOffreId(@PathVariable("appeloffreId") UUID appeloffreId) {
        List<Offre> offres = userService.listAllOffresByAppelOffreId(appeloffreId);
        return ResponseEntity.ok(offres);
    }
    @GetMapping("/offres/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID id) {
        Optional<AppelOffre> offreOptional = userService.getAppelOffreById(id);
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
