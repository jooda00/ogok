package com.ogok.ogok.email_verification;

import java.time.LocalDateTime;

import com.ogok.ogok.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class EmailVerification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String verificationCode;

	private boolean isVerified;

	private LocalDateTime expiresAt;

	private String email;

	public static EmailVerification of(String verificationCode, String email) {
		return EmailVerification.builder()
			.verificationCode(verificationCode)
			.isVerified(false)
			.expiresAt(LocalDateTime.now().plusMinutes(10))
			.email(email)
			.build();
	}

	public void setIsVerifiedTure() {
		this.isVerified = true;
	}
}
