package com.ogok.ogok.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

	private final UsersRepository usersRepository;

	public void saveUser(UsersReq usersReq) {
		log.info("사용자 저장 시작");
		Users user = Users.from(usersReq);
		if (usersRepository.findByEmail(user.getEmail()) != null) {
			throw new IllegalArgumentException("이미 사용 중인 이메일 입니다. 다른 이메일을 선택해주세요.");
		}
		usersRepository.save(user);
		log.info("사용자 저장 완료");
	}

	@Transactional
	public void updateUser(Long userId) {
		log.info("사용자 조회 시작");
		Users user = usersRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		log.info("사용자 조회 완료. 구독 취소 시작");
		user.cancelSubscription();
		log.info("구독 취소 완료");
	}
}
