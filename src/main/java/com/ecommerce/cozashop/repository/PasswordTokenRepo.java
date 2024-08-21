package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.PasswordResetToken;

@Repository
public interface PasswordTokenRepo extends JpaRepository<PasswordResetToken, Long> {
	PasswordResetToken findByToken(String token);
}
