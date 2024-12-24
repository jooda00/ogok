package com.ogok.ogok.email_verification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ogok.ogok.common.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailVerificationService {

	private final EmailVerificationRepository emailVerificationRepository;
	private final EmailService emailService;
	private final SpringTemplateEngine templateEngine;

	public void sendVerificationEmail(String email) {
		log.info("인증번호 생성 시작");
		String verificationCode = generateVerificationCode();
		log.info("인증번호 생성 완료");
		log.info("인증메일 전송 시작");
		emailService.sendMail(email, "오곡 인증 메일", setContext(verificationCode));
		log.info("인증메일 전송 성공");
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
