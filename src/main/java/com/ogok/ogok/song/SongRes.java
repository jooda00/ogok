package com.ogok.ogok.song;

import java.util.Arrays;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SongRes {

	private String title;
	private String singer;
	private List<String> introduction;
	private String youtubeLink;
	private SongGenre songGenre;

	public static SongRes from(Song song) {
		return SongRes.builder()
			.title(song.getTitle())
			.singer(song.getSinger())
			.introduction(splitIntroduction(song.getIntroduction()))
			.youtubeLink(song.getYoutubeLink())
			.songGenre(song.getSongGenre())
			.build();
	}

	private static List<String> splitIntroduction(String introduction) {
		if (introduction == null || introduction.isEmpty()) {
			return Arrays.asList("");
		}
		return Arrays.asList(introduction.split("\\.\\s*"));
	}

}
