package com.example.gestionressourcebanque.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("administrateur")

public class Administrateur extends Utilisateur {
    private double prime;
    private double salaireParJour;

    @OneToMany(mappedBy = "administrateur" , fetch = FetchType.LAZY)
    private List<Document> documentList=new ArrayList<>();

    @OneToMany(mappedBy = "administrateur" , fetch = FetchType.LAZY)
    private List<Materiel> materielList=new ArrayList<>();



}
