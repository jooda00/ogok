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
		Users users = Users.of(usersReq);
		usersRepository.save(users);
		log.info("사용자 저장 완료");
	}
}
