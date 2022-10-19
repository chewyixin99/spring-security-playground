package com.example.demo.verification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken findByToken(String token);
    
}
