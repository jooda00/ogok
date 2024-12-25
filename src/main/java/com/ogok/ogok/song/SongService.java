package com.ogok.ogok.song;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SongService {

	private final SongRepository songRepository;

	@Transactional
	public void saveSong(SongReq req) {
		Song song = Song.from(req);
		songRepository.save(song);
	}
}
