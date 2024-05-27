package com.auth.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "demandeajoutentreprise")
public class DemandeAjoutEntreprise {
	@Id
	@GeneratedValue(generator = "UUID")
    private UUID id;
    private String nom;
    private String adresse;
    private String secteuractivite;
    private String Matricule;
    private String ville;
    private String siegesociale;
    private String idcategorie;
    private String codeTVA;
    private UUID userId;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] codetvadocument;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] status;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] logo;
}
