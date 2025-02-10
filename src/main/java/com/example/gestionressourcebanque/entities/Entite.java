package com.example.gestionressourcebanque.entities;

import com.example.gestionressourcebanque.enums.TypeEntite;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    @Enumerated(EnumType.STRING)
    private TypeEntite type;

    @OneToMany(mappedBy = "entite" , fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entite_pere_id")
    private Entite entitePere;

    @OneToMany(mappedBy = "entitePere", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entite> entiteEnfants = new ArrayList<>();

    @AssertTrue(message = "L'entité ne peut pas être son propre parent")
    public boolean isValidEntitePere() {
        return entitePere== null || !entitePere.equals(this);
    }

    @AssertTrue(message = "Une entité ne peut pas être son propre enfant")
    public boolean isValidEntiteEnfant() {
        for (Entite entite : entiteEnfants) {
            if (entite.equals(this)) {
                return false;
            }
        }
        return true;
    }
}