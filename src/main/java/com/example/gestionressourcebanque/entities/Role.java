package com.example.gestionressourcebanque.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private List<Utilisateur> utilisateurs;


}
