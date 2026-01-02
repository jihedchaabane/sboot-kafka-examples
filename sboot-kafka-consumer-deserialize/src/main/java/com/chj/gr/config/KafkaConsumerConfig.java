package com.chj.gr.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.chj.gr.Person;
import com.chj.gr.rest.RequestDto;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;
	
	@Value("${app.kafka.groups.string-group}")
	private String stringGroupId;
	
	@Value("${app.kafka.groups.person-group}")
	private String personGroupId;
	
	@Value("${app.kafka.groups.requestDto-group}")
	private String requestDtoGroupId;
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> cunsumerListenerStringFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, stringGroupId);
		
		configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		ConcurrentKafkaListenerContainerFactory<String, String> factory 
				= new ConcurrentKafkaListenerContainerFactory<String, String>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, String>(configs));
		return factory;
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Person> cunsumerListenerPersonFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, personGroupId);
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
		configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		ConcurrentKafkaListenerContainerFactory<String, Person> factory 
				= new ConcurrentKafkaListenerContainerFactory<String, Person>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, Person>(configs));
		return factory;
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, RequestDto> cunsumerListenerRequestDtoFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, requestDtoGroupId);
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
		configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
		
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		ConcurrentKafkaListenerContainerFactory<String, RequestDto> factory 
				= new ConcurrentKafkaListenerContainerFactory<String, RequestDto>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, RequestDto>(configs));
		return factory;
	}
	
}