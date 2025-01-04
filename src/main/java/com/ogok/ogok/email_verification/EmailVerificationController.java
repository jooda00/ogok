package com.ogok.ogok.email_verification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogok.ogok.user.UsersReq;
import com.ogok.ogok.user.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
@Slf4j
public class EmailVerificationController {

	private final EmailVerificationService emailVerificationService;
	private final UsersService usersService;

	@PostMapping
	public ResponseEntity<String> subscribe(@RequestParam String email, @RequestParam String genre) {
		try {
			// 사용자 저장
			usersService.saveUser(new UsersReq(email, genre));
			// 인증 이메일 전송
			emailVerificationService.sendVerificationEmail(email);
			return ResponseEntity.ok("구독 신청이 완료되었습니다. 이메일을 확인해주세요.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/verified")
	public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String verificationCode) {
		try {
			// 인증 번호 처리
			emailVerificationService.verifyVerificationCode(email, verificationCode);
			return ResponseEntity.ok("인증 번호가 정상적으로 처리되었습니다. 서비스 사용이 가능합니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
