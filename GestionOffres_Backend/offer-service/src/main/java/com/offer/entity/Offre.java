package com.offer.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "offres")
public class Offre {
	@Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
	private String numtel;
	private double montant;
	private Date delaisderealisation; 
	private String entrepriseid;
	private String userid;
	@Lob
    @Column(columnDefinition = "longblob")
    private byte[] documentdeproposition;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datededepot = new Date();
	@ManyToOne
	@JoinColumn(name = "appeloffre_id", nullable = false)
	@JsonIgnore
	private AppelOffre appeloffre;
}
