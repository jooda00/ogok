package com.ogok.ogok.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UsersService {

	private final UsersRepository usersRepository;

	@Transactional
	public void saveUsers(UsersReq usersReq) {
		log.info("사용자 저장 시작");
		Users user = Users.of(usersReq);
		if (usersRepository.findByEmail(user.getEmail()) != null) {
			throw new IllegalArgumentException("이미 사용 중인 이메일 입니다. 다른 이메일을 선택해주세요.");
		}
		usersRepository.save(user);
		log.info("사용자 저장 완료");
	}
}
