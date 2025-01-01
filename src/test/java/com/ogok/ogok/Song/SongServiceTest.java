package com.ogok.ogok.Song;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ogok.ogok.song.Song;
import com.ogok.ogok.song.SongGenre;
import com.ogok.ogok.song.SongRepository;
import com.ogok.ogok.song.SongReqForTest;
import com.ogok.ogok.song.SongRes;
import com.ogok.ogok.song.SongService;
import com.ogok.ogok.song_history.SongHistory;
import com.ogok.ogok.song_history.SongHistoryRepository;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;
import com.ogok.ogok.user.UsersReq;

public class SongServiceTest {

	@InjectMocks
	private SongService songService;

	@Mock
	private SongRepository songRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private SongHistoryRepository songHistoryRepository;

	private String songGenre;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		songGenre = "신나는";
	}

	// @Test
	// @DisplayName("음악 디비 저장 테스트")
	// void saveSongTest() {
	// 	// given
	// 	SongReq req = new SongReq(1L, "바람", "윤하", "가수 윤하의 노래 입니다.",
	// 		"link", SongGenre.convertStringToSongGenre(songGenre));
	// 	Song song = Song.fromForTest(req);
	//
	// 	// when
	// 	songService.saveSong(req);
	//
	// 	// then
	// 	verify(songRepository, times(1)).save(ArgumentMatchers.refEq(song));
	// }

	@Test
	@DisplayName("사용자가 선택한 장르 음악 추출 쿼리 테스트")
	void querySongByUsersSongGenreTest() {
		// given
		String email = "test@test.com";
		Song song = Song.fromForTest(new SongReqForTest(1L, "바람", "윤하", "가수 윤하의 노래 입니다.",
			"link", SongGenre.convertStringToSongGenre(songGenre)));
		Users user = Users.from(new UsersReq(email, songGenre));

		when(usersRepository.findByEmail(email)).thenReturn(user);
		when(songRepository.findUnsentSong(SongGenre.convertStringToSongGenre(songGenre), user.getId())).thenReturn(song);

		// when
		SongRes songRes = songService.getSong(email);

		// then
		assertThat(songRes.getSongGenre()).isEqualTo(song.getSongGenre());
		assertThat(songRes.getTitle()).isEqualTo(song.getTitle());
		assertThat(user.getSongHistories().size()).isEqualTo(1);

		verify(songRepository, times(1)).findUnsentSong(SongGenre.convertStringToSongGenre(songGenre), user.getId());
		verify(songHistoryRepository, times(1)).save(user.getSongHistories().get(0));
	}

	@Test
	@DisplayName("사용자에게 전송되지 않은 음악 정상 전송 테스트")
	void getNotSentSongToUser() {
		// given
		String email = "test@test.com";
		Users user = Users.from(new UsersReq(email, songGenre));

		Song sentSong = Song.fromForTest(new SongReqForTest(1L, "바람", "윤하", "가수 윤하의 노래 입니다.",
			"link", SongGenre.convertStringToSongGenre(songGenre)));
		Song notSentSong = Song.fromForTest(new SongReqForTest(2L, "파도", "하현상", "가수 하현상 정규 1집 마지막 트랙의 곡입니다.",
			"link", SongGenre.convertStringToSongGenre(songGenre)));

		when(usersRepository.findByEmail(email)).thenReturn(user);
		when(songRepository.findUnsentSong(SongGenre.convertStringToSongGenre(songGenre), user.getId()))
			.thenReturn(sentSong)
			.thenReturn(notSentSong);

		// when: 첫 번째 곡 전송
		SongRes firstSongRes = songService.getSong(email);

		// then: 첫 번째 곡 확인
		assertThat(firstSongRes.getTitle()).isEqualTo(sentSong.getTitle());
		assertThat(user.getSongHistories().size()).isEqualTo(1);

		verify(songHistoryRepository, times(1)).save(any(SongHistory.class));

		// when: 두 번째 곡 전송
		SongRes secondSongRes = songService.getSong(email);

		// then: 두 번째 곡 확인
		assertThat(secondSongRes.getTitle()).isEqualTo(notSentSong.getTitle());
		assertThat(user.getSongHistories().size()).isEqualTo(2);

		verify(songHistoryRepository, times(2)).save(any(SongHistory.class));
	}
}
