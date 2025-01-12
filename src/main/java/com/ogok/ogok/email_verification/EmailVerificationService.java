package com.ogok.ogok.email_verification;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ogok.ogok.common.EmailService;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

	private final EmailVerificationRepository emailVerificationRepository;
	private final UsersRepository usersRepository;
	private final EmailService emailService;
	private final SpringTemplateEngine templateEngine;

	public void sendVerificationEmail(String email) {
		log.info("인증번호 생성 시작");
		String verificationCode = generateVerificationCode();
		log.info("인증번호 생성 완료");

		log.info("인증메일 전송 시작");
		emailService.sendMail(email, "오곡 인증 메일", setContext(verificationCode));
		log.info("인증메일 전송 성공");

		EmailVerification emailVerification = EmailVerification.of(verificationCode, email);
		emailVerificationRepository.save(emailVerification);
	}

	@Transactional
	public void verifyVerificationCode(String email, String submittedCode) {
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(email);
		if (emailVerification == null) {
			throw new IllegalArgumentException("인증정보가 존재하지 않습니다.");
		}
		if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("10분이 지나 인증번호가 만료되었습니다.");
		}
		if (!emailVerification.getVerificationCode().equals(submittedCode)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}

		log.info("인증번호 일치를 확인하고 인증 완료 상태로 변경");
		emailVerification.setIsVerifiedTure();

		log.info("이메일을 사용해 사용자 정보 추출");
		Users users = usersRepository.findByEmail(email);
		if (users == null) {
			throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
		}

		log.info("사용자 상태 구독 활성(ACTIVE) 상태로 변경");
		users.setStatusActive();
	}

	private String generateVerificationCode() {
		return String.valueOf((int) (Math.random() * 1000000));
	}

	private String setContext(String verificationCode) {
		Context context = new Context();
		context.setVariable("verificationCode", verificationCode);
		return templateEngine.process("cert", context);
	}
}
