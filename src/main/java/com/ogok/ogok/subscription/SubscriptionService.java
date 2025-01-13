package com.ogok.ogok.subscription;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ogok.ogok.email_verification.EmailVerificationService;
import com.ogok.ogok.user.UsersReq;
import com.ogok.ogok.user.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

	private final EmailVerificationService emailVerificationService;
	private final UsersService usersService;

	@Transactional
	public void subscribe(String email, String genre) {
		// 사용자 저장
		usersService.saveUser(new UsersReq(email, genre));
		// 인증 이메일 전송
		emailVerificationService.sendVerificationEmail(email);
	}
}
