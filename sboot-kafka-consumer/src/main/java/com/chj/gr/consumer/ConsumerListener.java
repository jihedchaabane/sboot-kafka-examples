package com.chj.gr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.chj.gr.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerListener {
	
	ObjectMapper mapper = new ObjectMapper();
	
	@KafkaListener(topics = "${app.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(ConsumerRecord<String, Object> message) {
		
		log.info("ConsumerRecord: key {}", message.key());
		log.info("ConsumerRecord: value {}", message.value());
		log.info("ConsumerRecord: topic {}", message.topic());
		log.info("ConsumerRecord: partition {}", message.partition());
		log.info("ConsumerRecord: offset {}", message.offset());
		log.info("ConsumerRecord: timestampType {}", message.timestampType().name);
		log.info("ConsumerRecord: timestamp {}", message.timestamp());
		
		message.headers().forEach(t -> {
			log.info("{} : {}", t.key(), new String(t.value()));
			if ("__TypeId__".equals(t.key())) {
				if (Person.class.getName().equals(new String(t.value()))) {
					log.info("It's a Person class");
					try {
						Person person = (Person) mapper.readValue(
								message.value().toString(), Class.forName(new String(t.value())));
						log.info(person.toString());
					} catch (JsonProcessingException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
