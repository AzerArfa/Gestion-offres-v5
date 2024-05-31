package com.auth.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
	@Id
	@GeneratedValue(generator = "UUID")
    private UUID id;
	  @ManyToOne
	    @JoinColumn(name = "entreprise_id", nullable = false)
	    @JsonBackReference
	    private Entreprise entreprise;
	private UUID id_appeloffre;
	private UUID id_categorie;
	 @Temporal(TemporalType.TIMESTAMP)
	    private Date creationDate = new Date();
	    
}
