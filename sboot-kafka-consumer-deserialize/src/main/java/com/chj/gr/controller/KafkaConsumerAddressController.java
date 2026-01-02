package com.chj.gr.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chj.gr.Address;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("ms6/receive")
@Slf4j
public class KafkaConsumerAddressController {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;
	
	@Value("${app.kafka.groups.address-group}")
	private String addressGroupId;
	
	@Value("${app.kafka.consumer.address-topic}")
	private String addressTopic;
	
	
	@GetMapping("/address")
	public String receiveAddress() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, addressGroupId);
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
		configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		try (KafkaConsumer<String, Address> kafkaConsumer = new KafkaConsumer<String, Address>(configs)) {
			kafkaConsumer.subscribe(Collections.singletonList(addressTopic));
			
			while (true) {
				ConsumerRecords<String, Address> records = kafkaConsumer.poll(Duration.ofMillis(1000));
				for (ConsumerRecord<String, Address> consumerRecord : records) {
					log.info("Message received is : {}", consumerRecord.value().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Received";
	}

}
