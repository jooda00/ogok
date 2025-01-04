package com.ogok.ogok.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ogok.ogok.common.EmailService;
import com.ogok.ogok.song.SongRes;
import com.ogok.ogok.song.SongService;
import com.ogok.ogok.user.Users;
import com.ogok.ogok.user.UsersRepository;
import com.ogok.ogok.user.UsersStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

	private final UsersRepository usersRepository;
	private final EmailService emailService;
	private final SongService songService;
	private final SpringTemplateEngine templateEngine;

	@Scheduled(cron = "0 0 8 * * 1-5", zone = "Asia/Seoul")
	public void sendSong() {
		log.info("구독 완료한 구독자 추출");
		List<Users> users = usersRepository.findAllByStatus(UsersStatus.ACTIVE);
		log.info("{} 명의 구독 완료한 구독자에게 곡 메일 발송 작업 시작", users.size());
		for (Users u : users) {
			try {
				String email = u.getEmail();
				SongRes res = songService.getSong(email);
				emailService.sendMail(email, "오늘의 추천 곡을 보내드려요.", setContext(res));
			} catch (Exception e) {
				log.error("사용자 {}에게 곡 전송 중 오류 발생: {}", u.getEmail(), e.getMessage());
			}
		}
	}

	private String setContext(SongRes res) {
		Context context = new Context();
		context.setVariable("res", res);
		return templateEngine.process("song", context);
	}
}
