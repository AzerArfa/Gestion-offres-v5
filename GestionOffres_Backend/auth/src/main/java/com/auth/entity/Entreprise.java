	package com.auth.entity;
	
	import java.util.ArrayList;
	import java.util.List;
	import java.util.UUID;
	
	import jakarta.persistence.CascadeType;
	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.FetchType;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.Lob;
	import jakarta.persistence.ManyToMany;
	import jakarta.persistence.Table;
	import lombok.Data;
	
	@Entity
	@Data
	@Table(name = "entreprises")
	public class Entreprise {
		@Id
		@GeneratedValue(generator = "UUID")
	    private UUID id;
	    private String nom;
	    private String adresse;
	    private String secteuractivite;
	    private String matricule;
	    private String ville;
	    private String siegesociale;
	    private String codeTVA;
	    private String idcategorie;
	    @Lob
	    @Column(columnDefinition = "longblob")
	    private byte[] codetvadocument;
	    @Lob
	    @Column(columnDefinition = "longblob")
	    private byte[] status;
	    @Lob
	    @Column(columnDefinition = "longblob")
	    private byte[] logo;
	    @ManyToMany(mappedBy = "entreprises", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	    private List<User> users = new ArrayList<>();
	}
