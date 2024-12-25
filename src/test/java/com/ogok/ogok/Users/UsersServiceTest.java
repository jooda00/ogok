package com.ogok.ogok.Users;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ogok.ogok.song.SongGenre;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;
import com.ogok.ogok.user.UsersReq;
import com.ogok.ogok.user.UsersService;
import com.ogok.ogok.user.UsersStatus;

public class UsersServiceTest {

	@InjectMocks
	private UsersService usersService; // 테스트 대상 클래스

	@Mock
	private UsersRepository usersRepository; // Mock 객체

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 초기화
	}

	@Test
	@DisplayName("사용자를 정상적으로 저장합니다.")
	void testSaveUser() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT); // 테스트용 요청 객체
		Users mockUser = Users.from(mockRequest);

		// when
		usersService.saveUser(mockRequest);

		// then
		verify(usersRepository, times(1)).save(ArgumentMatchers.refEq(mockUser)); // Repository의 save 메서드가 호출되었는지 확인
	}

	@Test
	@DisplayName("사용자가 서비스를 구독하면 인증 전 상태가 됩니다.")
	void testSaveUserIsActiveWhenSubscribe() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT);
		Users mockUser = Users.from(mockRequest);

		// when
		usersService.saveUser(mockRequest);

		// then
		assertThat(mockUser.getStatus()).isEqualTo(UsersStatus.PENDING);
	}

	@Test
	@DisplayName("구독 활성 상태에서 구독을 취소합니다.")
	void testCancelSubscription() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT);

		when(usersRepository.findById(1L)).thenReturn(Optional.of(
			Users.createActiveUser(1L, mockRequest)
		));

		usersService.updateUser(1L);

		// then
		assertThat(usersRepository.findById(1L).get().getStatus()).isEqualTo(UsersStatus.CANCELLED);
	}

	@Test
	@DisplayName("구독 전에는 구독을 취소할 수 없습니다.")
	void throwExceptionCancelSubscriptionWhenPending() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT);

		// thenReturn 사용해서 특정 응답 반환
		when(usersRepository.findById(1L)).thenReturn(Optional.of(
			Users.createWithId(1L, mockRequest)
		));

		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> usersService.updateUser(1L)
		);

		// then
		assertEquals("구독 전에는 구독을 취소할 수 없습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("이미 존재하는 이메일로는 구독을 신청할 수 없습니다.")
	void throwExceptionWhenUsingExistedEmail() {
		// given
		UsersReq existingUserReq = new UsersReq("test@test.com", SongGenre.UPBEAT);
		UsersReq newUserReq = new UsersReq("test@test.com", SongGenre.CALM);

		Users existingUser = Users.from(existingUserReq);

		when(usersRepository.findByEmail("test@test.com")).thenReturn(existingUser);

		// when
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> usersService.saveUser(newUserReq) // 예외가 발생해야 함
		);

		// then
		assertEquals("이미 사용 중인 이메일 입니다. 다른 이메일을 선택해주세요.", exception.getMessage());
		verify(usersRepository, times(0)).save(any(Users.class)); // save가 호출되지 않았는지 검증
	}
}
