package com.auth.services.auth;

import com.auth.entity.DemandeRejoindreEntreprise;
import com.auth.entity.User;
import com.auth.entity.Entreprise;
import com.auth.repository.DemandeRejointRepository;
import com.auth.repository.UserRepository;

import jakarta.persistence.EntityManager;

import com.auth.repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DemandeRejointServiceImpl implements DemandeRejointService {
	@Autowired
	private EntityManager entityManager;

    @Autowired
    private DemandeRejointRepository demandeRejointRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Override
    @Transactional
    public void createJoinRequest(UUID userId, String entrepriseMatricule) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Entreprise entreprise = entrepriseRepository.findByMatricule(entrepriseMatricule)
            .orElseThrow(() -> new RuntimeException("Entreprise not found"));

        DemandeRejoindreEntreprise joinRequest = new DemandeRejoindreEntreprise();
        joinRequest.setUserId(userId);
        joinRequest.setEntrepriseMatricule(entrepriseMatricule);
        demandeRejointRepository.save(joinRequest);
    }

    @Override
    @Transactional
    public void approveJoinRequest(UUID requestId) {
        DemandeRejoindreEntreprise joinRequest = demandeRejointRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Join request not found"));
        joinRequest.setStatus("APPROVED");
        demandeRejointRepository.save(joinRequest);

        User user = userRepository.findById(joinRequest.getUserId()).orElseThrow();
        Entreprise entreprise = entrepriseRepository.findByMatricule(joinRequest.getEntrepriseMatricule()).orElseThrow();

        // Directly insert user and entreprise relation into the join table
        userRepository.addUserToEntreprise(user.getId(), entreprise.getId());
    }
    @Override
    public void deleteRequest(UUID requestId) {
    	demandeRejointRepository.deleteById(requestId);
    }

    @Override
    @Transactional
    public void rejectJoinRequest(UUID requestId) {
        DemandeRejoindreEntreprise joinRequest = demandeRejointRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Join request not found"));
        joinRequest.setStatus("REJECTED");
        demandeRejointRepository.save(joinRequest);
    }
    @Override
    @Transactional
    public List<DemandeRejoindreEntreprise> getAllJoinRequests(String userId) {
        // Fetch all entrepriseMatricules associated with this user
        List<String> entrepriseMatricules = userRepository.findEntrepriseMatriculesByUserId(UUID.fromString(userId));

        // Retrieve all join requests for these entrepriseMatricules
        return demandeRejointRepository.findAllByEntrepriseMatriculeIn(entrepriseMatricules);
    }


}
