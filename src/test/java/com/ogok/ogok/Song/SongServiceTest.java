package com.ogok.ogok.Song;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ogok.ogok.song.Song;
import com.ogok.ogok.song.SongGenre;
import com.ogok.ogok.song.SongRepository;
import com.ogok.ogok.song.SongReq;
import com.ogok.ogok.song.SongService;

public class SongServiceTest {

	@InjectMocks
	private SongService songService;

	@Mock
	private SongRepository songRepository;

	private SongGenre songGenre;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		songGenre = SongGenre.SOULFUL;
	}

	@Test
	@DisplayName("음악 디비 저장 테스트")
	void saveSongTest() {
		// given
		SongReq req = new SongReq(1L, "바람", "윤하", "가수 윤하의 노래 입니다.",
			"link", songGenre);
		Song song = Song.from(req);

		// when
		songService.saveSong(req);

		// then
		verify(songRepository, times(1)).save(ArgumentMatchers.refEq(song));
	}
}
