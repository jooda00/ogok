package com.ogok.ogok.common;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailService {

	private final JavaMailSender javaMailSender;

	public void sendMail(String to, String subject, String content) {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
			mimeMessageHelper.setTo(to); // 메일 수신자
			mimeMessageHelper.setSubject(subject); // 메일 제목
			mimeMessageHelper.setText(content, true); // 메일 본문
			javaMailSender.send(mailMessage);
		}
		catch (Exception e) {
			throw new RuntimeException("메일 전송 실패", e);
		}
	}
}
