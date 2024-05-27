package com.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.Password;

public interface PasswordRepository extends JpaRepository<Password, UUID>{

}
