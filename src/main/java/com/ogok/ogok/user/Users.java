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
	private boolean status = false;

	public static Users of(UsersReq usersReq) {
		return Users.builder()
			.email(usersReq.getEmail())
			.songGenre(usersReq.getSongGenre())
			.status(true)
			.build();
	}
}
