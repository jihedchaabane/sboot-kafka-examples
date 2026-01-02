package com.chj.gr.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageProducer {
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void sendMessage(String topic, Object object) {
		Message<Object> message = MessageBuilder.withPayload(object)
			.setHeader(KafkaHeaders.TOPIC, topic)
		.build();
		
		ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(message);
		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
			
			@Override
			public void onSuccess(SendResult<String, Object> result) {
				
				log.info("Sent message success = [{}] with offset [{}]", message, result.getRecordMetadata().offset());
			}
			
			@Override
			public void onFailure(Throwable ex) {
				
				log.error("Sent message failed = [{}] due to [{}]", message, ex.getMessage());
			}
		});
	}
}