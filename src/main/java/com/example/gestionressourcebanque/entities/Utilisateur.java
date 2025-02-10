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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur")
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String address;
    private String telephone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @ManyToOne
    private Entite entite;

    @OneToMany(mappedBy = "utilisateur" , fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Demande> demandeList=new ArrayList<>();



}
