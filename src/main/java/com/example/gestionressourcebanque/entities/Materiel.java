package com.example.gestionressourcebanque.entities;

import com.example.gestionressourcebanque.enums.EtatMateriel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de matériel est obligatoire")
    private TypeMateriel type;

    @Enumerated(EnumType.STRING)
    private StatutMateriel statut = StatutMateriel.Libre;

    @Enumerated(EnumType.STRING)
    private EtatMateriel etat = EtatMateriel.Fonctionnel;

    @NotNull(message = "Le numéro de série est obligatoire")
    @Column(unique = true)
    private long num_serie;

    private String description_mat;
    private String site_A_concerne;
    private String site_B_concerne;
    private boolean entre_sortie_A;
    private boolean entre_sortie_B;
    private Date date_evenement_A;
    private Date date_evenement_B;

    @ManyToOne
    private Administrateur administrateur;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Demande demande;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Mouvement mouvement;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private String imageName;
    private String imageType;

    public String getImageBase64() {
        return Base64.getEncoder().encodeToString(this.image);
    }

    private Double prix_moy;
}
