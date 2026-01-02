package com.chj.gr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperConfig {

	@Bean
	private ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
