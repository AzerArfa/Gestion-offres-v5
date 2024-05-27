package com.auth.services.auth;

import java.util.List;
import java.util.UUID;

import com.auth.entity.DemandeRejoindreEntreprise;

public interface DemandeRejointService {
    void createJoinRequest(UUID userId, String entrepriseMatricule);
    void approveJoinRequest(UUID requestId) throws Exception;
    void rejectJoinRequest(UUID requestId) throws Exception;
	void deleteRequest(UUID requestId);
	List<DemandeRejoindreEntreprise> getAllJoinRequests(String userId);
	
}
