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
		String code = "123456";

		Users mockUser = Users.from(new UsersReq(email, "신나는"));
		EmailVerification mockVerification = EmailVerification.of(code,	email);

		when(emailVerificationRepository.findByEmail(email)).thenReturn(mockVerification);
		when(usersRepository.findByEmail(email)).thenReturn(mockUser);

		// when
		emailVerificationService.verifyVerificationCode(email, code);

		// then
		assertTrue(mockVerification.isVerified());
		assertEquals(UsersStatus.ACTIVE, mockUser.getStatus());
		verify(emailVerificationRepository, times(1)).findByEmail(email);
		verify(usersRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("잘못된 인증번호로 검증 시 예외 발생")
	void invalidVerificationCodeTest() {
		String email = "test@test.com";
		String correctCode = "123456";
		String incorrectCode = "654321";

		EmailVerification mockVerification = EmailVerification.of(correctCode, email);

		when(emailVerificationRepository.findByEmail(email)).thenReturn(mockVerification);

		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> emailVerificationService.verifyVerificationCode(email, incorrectCode)
		);

		assertEquals("인증번호가 일치하지 않습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("인증정보가 존재하지 않는 경우 예외 발생")
	void notExistedEmailVerificationTest() {
		String email = "test@test.com";
		String code = "123456";

		when(emailVerificationRepository.findByEmail(email)).thenReturn(null);

		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> emailVerificationService.verifyVerificationCode(email, code)
		);

		assertEquals("인증정보가 존재하지 않습니다.", exception.getMessage());
	}
}
