package com.auth.services.auth;
import com.auth.entity.Entreprise;
import com.auth.entity.Notification;
import com.auth.repository.NotificationRepository;
import com.auth.services.auth.NotificationService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
    private NotificationRepository notificationRepository;
  
  

   
   

    @Override
    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void deleteNotificationsByAppelOffreId(UUID idAppelOffre) {
        notificationRepository.deleteByIdAppeloffre(idAppelOffre);
    }
    @Override
    public List<Notification> getNotificationsByCategorieIdAndNotEntrepriseId(UUID categorieId, UUID entrepriseId) {
        return notificationRepository.findByCategorieIdAndNotEntrepriseId(categorieId, entrepriseId);
    }
    @Override
    public List<Notification> getNotificationsByAppelOffreId(UUID idAppelOffre) {
        return notificationRepository.findByIdAppeloffre(idAppelOffre);
    }
}
