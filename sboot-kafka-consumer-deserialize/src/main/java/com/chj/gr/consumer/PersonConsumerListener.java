package com.chj.gr.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.chj.gr.Person;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PersonConsumerListener {
	
	@KafkaListener(
			topics = "${app.kafka.consumer.person-topic}", 
			groupId = "${app.kafka.groups.person-group}",
			containerFactory = "cunsumerListenerPersonFactory"
	)
	public void listen(@Payload Person message, @Headers MessageHeaders messageHeaders) {
		
		log.info("Received message is : {}", message);
		
	}

}
