package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Notification;
import com.example.gestionressourcebanque.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void addNotification(String message, Long utilisateurId) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notification.setRead(false);
        notification.setUtilisateurId(utilisateurId);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdAndRead(utilisateurId, false);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
