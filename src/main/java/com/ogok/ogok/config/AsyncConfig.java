package com.ogok.ogok.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ogok.ogok.exception.AsyncExceptionHandler;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	private AsyncExceptionHandler asyncExceptionHandler;

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2); // 기본 스레드 수. 항상 유지됨
		executor.setMaxPoolSize(5); // 스레드 풀에서 생성할 수 있는 최대 스레드 수
		executor.setQueueCapacity(10); // 대기열 크기
		executor.setThreadNamePrefix("Async MailExecutor-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return asyncExceptionHandler;
	}
}
