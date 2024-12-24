package com.ogok.ogok.EmailVerification;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ogok.ogok.common.EmailService;
import com.ogok.ogok.email_verification.EmailVerificationRepository;
import com.ogok.ogok.email_verification.EmailVerificationService;

public class EmailVerificationServiceTest {

	@InjectMocks
	private EmailVerificationService emailVerificationService; // 테스트 대상 클래스

	@Mock
	private EmailService emailService;

	@Mock
	private SpringTemplateEngine templateEngine;

	@Mock
	private EmailVerificationRepository emailVerificationRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 초기화
	}

	@Test
	@DisplayName("인증 메일 전송 테스트")
	void sendVerificationEmailTest() {
		// given
		String email = "test@test.com";
		String mockTemplateResult = "<html>인증번호: 123456</html>";

		// TemplateEngine Mock 동작 정의
		when(templateEngine.process(eq("cert"), any(Context.class))).thenReturn(mockTemplateResult);

		// EmailService Mock 동작 정의
		doNothing().when(emailService).sendMail(email, "오곡 인증 메일", mockTemplateResult);

		// when
		emailVerificationService.sendVerificationEmail(email);

		// then
		verify(templateEngine, times(1)).process(eq("cert"), any(Context.class)); // 템플릿 처리 확인
		verify(emailService, times(1)).sendMail(email, "오곡 인증 메일", mockTemplateResult); // 이메일 전송 확인
	}
}
