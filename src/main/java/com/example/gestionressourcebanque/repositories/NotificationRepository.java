package com.example.gestionressourcebanque.repositories;

import com.example.gestionressourcebanque.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateurIdAndRead(Long utilisateurId, boolean read);
}
