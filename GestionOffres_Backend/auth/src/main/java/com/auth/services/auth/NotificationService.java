package com.auth.services.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth.entity.Notification;

@Service
public interface NotificationService {
	    Notification addNotification(Notification notification);
	    void deleteNotification(UUID id);
	    List<Notification> getNotificationsByCategorieIdAndNotEntrepriseId(UUID categorieId, UUID entrepriseId);
		List<Notification> getNotificationsByAppelOffreId(UUID idAppelOffre);
		void deleteNotificationsByAppelOffreId(UUID idAppelOffre);

}
