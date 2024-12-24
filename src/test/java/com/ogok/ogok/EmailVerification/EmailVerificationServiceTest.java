package com.ogok.ogok.EmailVerification;

import static org.junit.jupiter.api.Assertions.*;
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
import com.ogok.ogok.email_verification.EmailVerification;
import com.ogok.ogok.email_verification.EmailVerificationRepository;
import com.ogok.ogok.email_verification.EmailVerificationService;
import com.ogok.ogok.song.SongGenre;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;
import com.ogok.ogok.user.UsersReq;
import com.ogok.ogok.user.UsersStatus;

public class EmailVerificationServiceTest {

	@InjectMocks
	private EmailVerificationService emailVerificationService;

	@Mock
	private EmailService emailService;

	@Mock
	private SpringTemplateEngine templateEngine;

	@Mock
	private EmailVerificationRepository emailVerificationRepository;

	@Mock
	private UsersRepository usersRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
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

	@Test
	@DisplayName("인증번호 검증 및 사용자 상태 변경 테스트")
	void verifyVerificationCodeTest() {
		// given
		String email = "test@test.com";
		String verificationCode = "123456";

		Users mockUser = Users.of(new UsersReq(email, SongGenre.UPBEAT));
		EmailVerification mockVerification = EmailVerification.of(verificationCode,	email);

		when(emailVerificationRepository.findByEmail(email)).thenReturn(mockVerification);
		when(usersRepository.findByEmail(email)).thenReturn(mockUser);

		// when
		emailVerificationService.verifyVerificationCode(email, verificationCode);

		// then
		assertTrue(mockVerification.isVerified());
		assertEquals(UsersStatus.ACTIVE, mockUser.getStatus());
		verify(emailVerificationRepository, times(1)).findByEmail(email);
		verify(usersRepository, times(1)).findByEmail(email);
	}
}
