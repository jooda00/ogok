package com.ogok.ogok.song;

import lombok.Getter;

@Getter
public enum SongGenre {

	UPBEAT("신나는"),
	CALM("잔잔한"),
	SOULFUL("감성적인"),
	INTENSE("강렬한"),
	RELAXING("편안한");

	private String genre;

	SongGenre(String genre) {
		this.genre = genre;
	}
}
