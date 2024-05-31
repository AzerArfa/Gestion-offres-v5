package com.offer.services.admin;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.entity.Offre;
import com.offer.repository.AppelOffreRepository;
import com.offer.repository.CategorieRepository;
import com.offer.repository.CategorieRepository;
import com.offer.repository.OffreRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	 @Autowired
    private final AppelOffreRepository appelOffreRepository;
	 @Autowired
    private OffreRepository offreRepository;
	 @Autowired
	 private final CategorieRepository categorieRepository;

	 @PostConstruct
	    public void initCategories() {
	        createCategoryIfNotExists("Développement Web", "Création de sites web front-end et back-end, développement de solutions e-commerce, et conception de sites web attractifs et fonctionnels.");
	        createCategoryIfNotExists("Développement Mobile", "Développement d'applications mobiles pour iOS et Android, applications multiplateformes, et jeux mobiles.");
	        createCategoryIfNotExists("DevOps", "Intégration continue, déploiement continu (CI/CD), gestion d'infrastructure avec des outils comme Docker et Kubernetes, et services cloud.");
	        createCategoryIfNotExists("Cybersécurité", "Sécurité des réseaux, sécurité des applications, audits de sécurité, tests de pénétration, et gestion des identités et des accès.");
	        createCategoryIfNotExists("Développement Logiciel", "Développement d'applications de bureau, systèmes embarqués, développement de logiciels sur mesure, et intégration de systèmes.");
	        createCategoryIfNotExists("Science des Données et Analyse", "Analyse des données, apprentissage automatique, intelligence artificielle, et visualisation des données pour la prise de décision.");
	        createCategoryIfNotExists("Infrastructure IT", "Conception et mise en œuvre de réseaux, gestion des serveurs, virtualisation, solutions de stockage, et support informatique.");
	        createCategoryIfNotExists("Informatique en Nuage", "Architecture cloud, migration vers le cloud, optimisation des services cloud, sécurité cloud, et solutions SaaS, PaaS, IaaS.");
	        createCategoryIfNotExists("Marketing Digital et SEO", "Optimisation pour les moteurs de recherche (SEO), publicité payante (PPC), marketing sur les réseaux sociaux, et marketing de contenu.");
	        createCategoryIfNotExists("Blockchain et Cryptomonnaie", "Développement blockchain, contrats intelligents, création de cryptomonnaies, et applications décentralisées (DApps).");
	        createCategoryIfNotExists("IoT (Internet des Objets)", "Développement d'applications IoT, technologie portable, et solutions IoT pour améliorer la connectivité et l'automatisation.");
	    }
	 @Override
	    public Optional<Categorie> getCategorieById(UUID id) {
	        return categorieRepository.findById(id);
	    }
	  private void createCategoryIfNotExists(String name, String description) {
	        List<Categorie> existingCategories = categorieRepository.findAll();
	        boolean exists = existingCategories.stream().anyMatch(c -> c.getNomcategorie().equals(name));
	        if (!exists) {
	            Categorie newCategory = new Categorie();
	            newCategory.setNomcategorie(name);
	            newCategory.setDescription(description);
	            categorieRepository.save(newCategory);
	        }
	    }
	  @Override
	  public List<Categorie> getAllCategories() {
	        return categorieRepository.findAll();
	    }
	  @Override
	    public List<AppelOffre> getAppelOffresByCategorieId(UUID categorieId) {
	        return appelOffreRepository.findByCategorieId(categorieId);
	    }

    @Override
    public AppelOffre createAppelOffre(AppelOffre appelOffre) {
        try {
            AppelOffre newAppelOffer = new AppelOffre();
            newAppelOffer.setTitre(appelOffre.getTitre());
            newAppelOffer.setDescription(appelOffre.getDescription());
            newAppelOffer.setDatelimitesoumission(appelOffre.getDatelimitesoumission());
            newAppelOffer.setEntrepriseId(appelOffre.getEntrepriseId());
            newAppelOffer.setImg(appelOffre.getImg());
            newAppelOffer.setLocalisation(appelOffre.getLocalisation());
            newAppelOffer.setDocument(appelOffre.getDocument());
            return appelOffreRepository.save(appelOffre);
        } catch (Exception e) {
            return null;
        }

    }
    public boolean canUpdateAppelOffre(UUID appelOffreId) {
        List<Offre> offres = offreRepository.findByAppeloffre_Id(appelOffreId);
        boolean canUpdate = offres.isEmpty();
        System.out.println("canUpdateAppelOffre check for ID " + appelOffreId + ": " + canUpdate);
        return canUpdate; // Returns true if there are no offers, indicating update is allowed
    }
    public boolean canDeleteAppelOffre(UUID appelOffreId) {
        AppelOffre appelOffre = appelOffreRepository.findById(appelOffreId)
                .orElseThrow(() -> new IllegalArgumentException("AppelOffre not found"));
        List<Offre> offres = offreRepository.findByAppeloffre_Id(appelOffreId);
        boolean canDelete = (offres.isEmpty() && appelOffre.getDatelimitesoumission().after(new Date())) || appelOffre.getDatelimitesoumission().before(new Date());
        System.out.println("canDeleteAppelOffre check for ID " + appelOffreId + ": " + canDelete);
        return canDelete; // Returns true if there are no offers and the deadline hasn't passed, or if the deadline has passed
    }
    @Override
    public List<Offre> getOffresByUserId(String userid) {
        return offreRepository.findByUserid(userid);
    }
    @Override
    public List<AppelOffre> getAllAppelOffres() {
        return appelOffreRepository.findAll();
    }

    @Override
    public Optional<AppelOffre> getAppelOffreById(UUID id) {
        return appelOffreRepository.findById(id);
    }

    @Override
    public AppelOffre updateAppelOffre(UUID appelOffreId, AppelOffre updatedAppelOffre) {
        if (canUpdateAppelOffre(appelOffreId)) {
            return appelOffreRepository.save(updatedAppelOffre);
        } else {
            throw new IllegalStateException("Cannot update AppelOffre as there are related Offres");
        }
    }
    @Override
    public void deleteAppelOffre(UUID appelOffreId) {
        if (canDeleteAppelOffre(appelOffreId)) {
            appelOffreRepository.deleteById(appelOffreId);
        } else {
            throw new IllegalStateException("Cannot delete AppelOffre as there are related Offres");
        }
    }
    @Override
    public List<AppelOffre> getAppelOffresByEntrepriseId(UUID entrepriseId) {
        return appelOffreRepository.findByEntrepriseId(entrepriseId);
    }
    
//    GESTION OFFRE
    
    @Override
    public Offre createOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    
    @Override
    public Offre updateOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    @Override
    public void deleteOffre(UUID id) {
        offreRepository.deleteById(id);
    }

    @Override
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    @Override
    public Optional<Offre> getOffreById(UUID id) {
        return offreRepository.findById(id);
    }

    @Override
    public List<Offre> listAllOffresByAppelOffreId(UUID appelOffreId) {
        return offreRepository.findByAppeloffre_Id(appelOffreId);
    }
}
