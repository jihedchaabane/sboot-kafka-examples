package com.chj.gr.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StringConsumerListener {
	
	@KafkaListener(
			topics = "${app.kafka.consumer.string-topic}", 
			groupId = "${app.kafka.groups.string-group}",
			containerFactory = "cunsumerListenerStringFactory"
	)
	public void listen(@Payload String message, @Headers MessageHeaders messageHeaders) {
		
		log.info("Received message is : {}", message);
		
	}

}
