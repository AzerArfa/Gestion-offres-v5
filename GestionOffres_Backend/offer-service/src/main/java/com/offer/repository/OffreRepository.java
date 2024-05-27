package com.offer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.offer.entity.Offre;
@Repository
public interface OffreRepository extends JpaRepository<Offre, UUID>{
	  List<Offre> findByAppeloffre_Id(UUID appelOffreId);
	  List<Offre> findByUserid(String userid);
}
