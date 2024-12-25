package com.ogok.ogok.song;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ogok.ogok.song_history.SongHistory;
import com.ogok.ogok.song_history.SongHistoryRepository;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SongService {

	private final SongRepository songRepository;
	private final UsersRepository usersRepository;
	private final SongHistoryRepository songHistoryRepository;

	@Transactional
	public void saveSong(SongReq req) {
		Song song = Song.from(req);
		songRepository.save(song);
	}

	@Transactional
	public SongRes getSong(String email) {
		Users users = usersRepository.findByEmail(email);
		if (users == null) {
			throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
		}
		SongGenre songGenre = users.getSongGenre();

		log.info("사용자 장르에 맞는 곡 추출 시작");
		Song song = songRepository.findUnsentSong(songGenre, users.getId());
		log.info("사용자 장르에 맞는 곡 추출 완료");

		log.info("사용자에게 곡 전송 기록 저장 시작");
		SongHistory songHistory = new SongHistory(song, users);
		songHistoryRepository.save(songHistory);
		log.info("사용자에게 곡 전송 기록 저장 완료");

		SongRes songRes = SongRes.from(song);
		return songRes;
	}
}
