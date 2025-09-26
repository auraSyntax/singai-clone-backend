package com.aura.syntax.pos.management;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@SpringBootApplication
public class PosManagementApplication {
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(PosManagementApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set JVM default timezone to Sri Lanka
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Colombo"));
	}

}
