package com.ogok.ogok.Users;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	void testSaveUsers() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT); // 테스트용 요청 객체
		Users mockUser = Users.of(mockRequest);

		// when
		usersService.saveUsers(mockRequest);

		// then
		verify(usersRepository, times(1)).save(ArgumentMatchers.refEq(mockUser)); // Repository의 save 메서드가 호출되었는지 확인
	}

	@Test
	@DisplayName("사용자가 서비스를 구독하면 활성 상태가 됩니다.")
	void testSaveUsersIsActiveWhenSubscribe() {
		// given
		UsersReq mockRequest = new UsersReq("test@test.com", SongGenre.UPBEAT);
		Users mockUser = Users.of(mockRequest);

		// when
		usersService.saveUsers(mockRequest);

		// then
		assertThat(mockUser.isStatus()).isEqualTo(true);
	}

	@Test
	@DisplayName("이미 존재하는 이메일로는 구독을 신청할 수 없습니다.")
	void throwExcpetionWhenUsingExistedEmail() {
		// given
		UsersReq existingUserReq = new UsersReq("test@test.com", SongGenre.UPBEAT);
		UsersReq newUserReq = new UsersReq("test@test.com", SongGenre.CALM);

		Users existingUser = Users.of(existingUserReq);

		when(usersRepository.findByEmail("test@test.com")).thenReturn(existingUser);

		// when
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> usersService.saveUsers(newUserReq) // 예외가 발생해야 함
		);

		// then
		assertEquals("이미 사용 중인 이메일 입니다. 다른 이메일을 선택해주세요.", exception.getMessage());
		verify(usersRepository, times(0)).save(any(Users.class)); // save가 호출되지 않았는지 검증
	}
}
