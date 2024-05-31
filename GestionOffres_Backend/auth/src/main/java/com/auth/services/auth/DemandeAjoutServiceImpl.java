package com.auth.services.auth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.entity.DemandeAjoutEntreprise;
import com.auth.entity.Entreprise;
import com.auth.repository.DemandeAjoutRepository;
import com.auth.repository.EntrepriseRepository;

@Service
public class DemandeAjoutServiceImpl implements DemandeAjoutService {
	@Autowired
    private DemandeAjoutRepository repository;
	@Autowired
	EntrepriseRepository entrepriseRepository;

	@Override
	public DemandeAjoutEntreprise saveRequest(DemandeAjoutEntreprise request) {
	
	    Optional<Entreprise> existingEntreprise = entrepriseRepository.findByMatricule(request.getMatricule());
	    if (!existingEntreprise.isPresent()) {
	    
	        return repository.save(request);
	    } else {
	    
	        throw new IllegalArgumentException("An enterprise with the same matricule already exists");
	    }
	}
	   @Override
	    public Optional<DemandeAjoutEntreprise> getRequestById(UUID id) {
	        return repository.findById(id);
	    }
    @Override
    public List<DemandeAjoutEntreprise> getAllPendingRequests() {
        return repository.findAll();
    }
    @Override
    public void deleteRequest(UUID requestId) {
    	repository.deleteById(requestId);
    }

    @Override
    public DemandeAjoutEntreprise approveRequest(UUID requestId) {
    	DemandeAjoutEntreprise request = repository.findById(requestId).get();
        repository.save(request);
        return (request);
    }

    @Override
    public void rejectRequest(UUID requestId) {
    	DemandeAjoutEntreprise request = repository.findById(requestId).get();
        repository.save(request);
    }
    @Override
    public List<DemandeAjoutEntreprise> getRequestsByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }
}
