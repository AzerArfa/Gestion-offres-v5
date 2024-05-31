package com.auth.services.auth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth.entity.DemandeAjoutEntreprise;

@Service
public interface DemandeAjoutService {
	DemandeAjoutEntreprise saveRequest(DemandeAjoutEntreprise request);
	    List<DemandeAjoutEntreprise> getAllPendingRequests();
	    DemandeAjoutEntreprise approveRequest(UUID requestId);
	    void rejectRequest(UUID requestId);
		void deleteRequest(UUID requestId);
		 List<DemandeAjoutEntreprise> getRequestsByUserId(UUID userId);
		 Optional<DemandeAjoutEntreprise> getRequestById(UUID id);
}
