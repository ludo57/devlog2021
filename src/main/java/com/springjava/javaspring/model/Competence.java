package com.springjava.javaspring.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.springjava.javaspring.view.JsonViewCustom;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({JsonViewCustom.VueUtilisateur.class,JsonViewCustom.VueCompetence.class})
    private int id;

    @JsonView({JsonViewCustom.VueUtilisateur.class,JsonViewCustom.VueCompetence.class})
    private String nom;

    private String competence;


    @ManyToMany(mappedBy = "listeCompetence")
    @JsonView({JsonViewCustom.VueCompetence.class})
    private List<Utilisateur> utilisateurs;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }
}


