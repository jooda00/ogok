package com.ogok.ogok.song;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {

	Song findByEmail(String email);
}
