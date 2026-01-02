package com.chj.gr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EndpointsListener {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		requestMappingHandlerMapping.getHandlerMethods().forEach(
				(key, value) -> log.info("{} {}", key, value));
	}
}
