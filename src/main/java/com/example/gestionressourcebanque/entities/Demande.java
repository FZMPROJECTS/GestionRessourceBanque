package com.example.gestionressourcebanque.entities;

import com.example.gestionressourcebanque.enums.StatutDemande;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @Column(name = "date_demande")
    private LocalDate dateDemande = LocalDate.now();

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToOne
    @JoinColumn(name="materiel_id")
    private Materiel materiel;
}
