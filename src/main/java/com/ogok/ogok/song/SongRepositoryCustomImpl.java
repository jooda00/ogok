package com.ogok.ogok.song;

import org.springframework.stereotype.Component;

import com.ogok.ogok.song_history.QSongHistory;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SongRepositoryCustomImpl implements SongRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final QSong qSong = QSong.song;
	private final QSongHistory qSongHistory = QSongHistory.songHistory;

	@Override
	public Song findUnsentSong(SongGenre songGenre, Long userId) {
		return jpaQueryFactory
			.selectFrom(qSong)
			.where(qSong.songGenre.eq(songGenre)
				.and(JPAExpressions.selectFrom(qSongHistory)
				.where(qSongHistory.users.id.eq(userId).and(qSongHistory.song.id.eq(qSong.id))).notExists()))
			.fetchFirst();
	}
}
