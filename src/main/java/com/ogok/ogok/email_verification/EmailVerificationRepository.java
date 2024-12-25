package com.ogok.ogok.email_verification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
	EmailVerification findByEmail(String email);
}
