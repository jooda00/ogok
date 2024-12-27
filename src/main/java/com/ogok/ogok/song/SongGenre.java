package com.ogok.ogok.song;

import lombok.Getter;

@Getter
public enum SongGenre {

	UPBEAT("신나는"),
	INTENSE("강렬한"),
	CALM("잔잔한"),
	SOULFUL("감성적인");

	private String description;

	SongGenre(String description) {
		this.description = description;
	}

	public static SongGenre convertStringToSongGenre(String value) {
		for (SongGenre genre : SongGenre.values()) {
			if (genre.getDescription().equals(value)) {
				return genre;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 장르 값: " + value);
	}
}
