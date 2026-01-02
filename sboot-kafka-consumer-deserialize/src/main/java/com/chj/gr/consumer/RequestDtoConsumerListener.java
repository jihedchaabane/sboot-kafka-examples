package com.chj.gr.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.chj.gr.rest.RequestDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestDtoConsumerListener {
	
	@KafkaListener(
			topics = "${app.kafka.consumer.requestDto-topic}", 
			groupId = "${app.kafka.groups.requestDto-group}",
			containerFactory = "cunsumerListenerRequestDtoFactory"
	)
	public void listen(@Payload RequestDto requestDto, @Headers MessageHeaders messageHeaders) {
//		log.info("Received message is : {}", requestDto);
		
		log.info("{} rows of chunk {}."
				, requestDto.getData().size()
				, requestDto.getData().get(0).getChunkId());
	}

}
