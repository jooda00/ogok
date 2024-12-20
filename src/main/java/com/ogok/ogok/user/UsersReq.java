package com.ogok.ogok.user;

import com.ogok.ogok.song.SongGenre;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersReq {

	private String email;
	private SongGenre songGenre;
}
