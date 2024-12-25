package com.ogok.ogok.song;

import com.ogok.ogok.common.BaseEntity;

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
public class Song extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "song_id")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String singer;

	@Column(nullable = false)
	private String introduction;

	private String youtubeLink;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SongGenre songGenre;

	public static Song from(SongReq req) {
		return Song.builder()
			.id(req.getId())
			.title(req.getTitle())
			.singer(req.getSinger())
			.introduction(req.getIntroduction())
			.youtubeLink(req.getYoutubeLink())
			.songGenre(req.getSongGenre())
			.build();
	}
}
