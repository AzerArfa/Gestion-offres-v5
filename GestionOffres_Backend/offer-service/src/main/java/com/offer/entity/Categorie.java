package com.offer.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Categorie {
	   @Id
	    @GeneratedValue(generator = "UUID")
	    private UUID id;
	   private String description;
	    private String nomcategorie;
	    @OneToMany(mappedBy = "categorie")
	    private Set<AppelOffre> appeloffres = new HashSet<>();
}
