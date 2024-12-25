package com.ogok.ogok.song;

public interface SongRepositoryCustom {

	Song findUnsentSong(SongGenre songGenre, Long userId);
}
