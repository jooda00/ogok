package com.ogok.ogok.song;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

	private final SongService songService;

	@PostMapping
	public void saveSong(@RequestBody SongReq songReq) {
		songService.saveSong(songReq);
	}
}
