package com.ogok.ogok.Users;

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
}
