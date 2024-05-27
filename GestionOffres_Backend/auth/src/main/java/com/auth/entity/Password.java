package com.auth.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "passwords")
public class Password {
	@Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
	
	 @Temporal(TemporalType.TIMESTAMP)
	    private Date creationDate = new Date();
	    
	 @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
	 private String password;
}
