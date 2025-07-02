package com.health.ai_prediction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// AiPredictionServiceApplication.java
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AiPredictionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AiPredictionServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}


