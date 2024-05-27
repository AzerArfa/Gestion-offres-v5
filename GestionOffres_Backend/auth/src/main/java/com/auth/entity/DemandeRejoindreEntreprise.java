package com.auth.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "demanderejoinentreprises")
public class DemandeRejoindreEntreprise {
	@Id
	@GeneratedValue(generator = "UUID")
    private UUID id;
	
	 private UUID userId;
	 
	    private String entrepriseMatricule;
	    private String status;
}
