package com.offer.services.user;

import com.offer.entity.AppelOffre;
import com.offer.entity.Categorie;
import com.offer.entity.Offre;
import com.offer.repository.AppelOffreRepository;
import com.offer.repository.CategorieRepository;
import com.offer.repository.OffreRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
    private final AppelOffreRepository appelOffreRepository;
	 @Autowired
    private OffreRepository offreRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    public List<AppelOffre> getAllAppelOffres() {
        return appelOffreRepository.findAll();
    }
    @Override
    public Optional<AppelOffre> getAppelOffreById(UUID id) {
        return appelOffreRepository.findById(id);
    }
    @Override
    public Offre createOffre(Offre offre) {
        return offreRepository.save(offre);
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
    public List<Offre> getOffresByUserId(String userid) {
        return offreRepository.findByUserid(userid);
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
