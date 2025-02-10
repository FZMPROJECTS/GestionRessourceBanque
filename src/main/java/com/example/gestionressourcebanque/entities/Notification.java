package com.example.gestionressourcebanque.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(name = "date") // Préciser le nom de la colonne pour la date
    private LocalDateTime date;

    @Column(name = "read_status") // Spécifiez un nom de colonne différent
    private boolean read;

    private Long utilisateurId;

}
