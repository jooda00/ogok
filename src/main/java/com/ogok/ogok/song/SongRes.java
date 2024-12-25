package com.ogok.ogok.song;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SongRes {

	private String title;
	private String singer;
	private String introduction;
	private String youtubeLink;
	private SongGenre songGenre;

	public static SongRes from(Song song) {
		return SongRes.builder()
			.title(song.getTitle())
			.singer(song.getSinger())
			.introduction(song.getIntroduction())
			.youtubeLink(song.getYoutubeLink())
			.songGenre(song.getSongGenre())
			.build();
	}
}
