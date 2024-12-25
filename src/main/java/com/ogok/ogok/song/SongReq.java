package com.ogok.ogok.song;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SongReq {

	private Long id;
	private String title;
	private String singer;
	private String introduction;
	private String youtubeLink;
	private SongGenre songGenre;
}
