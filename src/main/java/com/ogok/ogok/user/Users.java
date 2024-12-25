package com.ogok.ogok.user;

import com.ogok.ogok.common.BaseEntity;
import com.ogok.ogok.song.SongGenre;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Users extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SongGenre songGenre;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UsersStatus status;

	public static Users of(UsersReq usersReq) {
		return Users.builder()
			.email(usersReq.getEmail())
			.songGenre(usersReq.getSongGenre())
			.status(UsersStatus.PENDING)
			.build();
	}

	public static Users createWithId(Long id, UsersReq usersReq) {
		return Users.builder()
			.id(id)
			.email(usersReq.getEmail())
			.songGenre(usersReq.getSongGenre())
			.status(UsersStatus.PENDING)
			.build();
	}

	public static Users createActiveUser(Long id, UsersReq usersReq) {
		return Users.builder()
			.id(id)
			.email(usersReq.getEmail())
			.songGenre(usersReq.getSongGenre())
			.status(UsersStatus.ACTIVE)
			.build();
	}

	public void cancelSubscription() {
		if (UsersStatus.PENDING.equals(this.status)) {
			throw new IllegalArgumentException("구독 전에는 구독을 취소할 수 없습니다.");
		}
		this.status = UsersStatus.CANCELLED;
	}

	public void setStatusActive() {
		this.status = UsersStatus.ACTIVE;
	}
}
