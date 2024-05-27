package com.auth.repository;

import com.auth.entity.Entreprise;
import com.auth.entity.User;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmail(String email);
    Optional<User> findFirstByEmail(String email);
    Optional<User> findByName(String username);
    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEntreprisesContains(Entreprise entreprise);
    @Query("SELECT e.matricule FROM Entreprise e JOIN e.users u WHERE u.id = :userId")
    List<String> findEntrepriseMatriculesByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_entreprises (user_id, entreprise_id) VALUES (:userId, :entrepriseId)", nativeQuery = true)
    void addUserToEntreprise(UUID userId, UUID entrepriseId);
}
