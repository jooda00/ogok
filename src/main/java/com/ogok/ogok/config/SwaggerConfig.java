package com.ogok.ogok.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.addServersItem(new Server().url("/").description("현재 서버"))
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("Ogok API Docs")
			.description("노래 추천 메일 구독 서비스")
			.version("1.0.0");
	}
}
