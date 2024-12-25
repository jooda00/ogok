package com.ogok.ogok.song_history;

import com.ogok.ogok.common.BaseEntity;
import com.ogok.ogok.song.Song;
import com.ogok.ogok.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class SongHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "song_id")
	private Song song;

	public SongHistory(Song song, Users users) {
		this.song = song;
		setUsers(users);
	}

	public void setUsers(Users user) {
		// 기존 users와의 연관 관계 제거
		if (this.users != null) {
			this.users.getSongHistories().remove(this);
		}

		this.users = user;

		user.addSongHistory(this);
	}
}
