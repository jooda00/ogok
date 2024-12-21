package com.ogok.ogok.user;

public enum UsersStatus {
	PENDING("인증 전"),
	ACTIVE("구독 활성"),
	CANCELLED("구독 취소");

	private String message;

	UsersStatus(String message) {
		this.message = message;
	}
}
