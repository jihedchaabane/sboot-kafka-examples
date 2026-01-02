package com.chj.gr.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chj.gr.Address;
import com.chj.gr.Person;
import com.chj.gr.producer.MessageProducer;
import com.chj.gr.rest.RequestDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ms6/topics/bytype")
@Slf4j
public class KafkaProducerTopicByDataTypeController {

	@Value("${app.kafka.producer.string-topic}")
	private String stringTopic;
	
	@Value("${app.kafka.producer.person-topic}")
	private String personTopic;
	
	@Value("${app.kafka.producer.address-topic}")
	private String addressTopic;
	
	@Value("${app.kafka.producer.requestDto-topic}")
	private String requestDtoTopic;

	@Autowired
	private MessageProducer messageProducer;

	/**
	 * 
	 */
	@GetMapping("/send/string")
	public String sendString(@RequestParam("message") String message) {

		messageProducer.sendMessage(stringTopic, message);
		return "Message sent";
	}
	
	/**
	 * 
	 */
	@GetMapping("/send/person")
	public String sendPerson(
			@RequestParam("firstName") String firstName, 
			@RequestParam("lastName") String lastName) {

		messageProducer.sendMessage(personTopic,
				Person.builder()
				.firstName(firstName)
				.lastName(lastName)
				.birthDate(new Date())
				.address(Address.builder()
						.road("road")
						.city("city")
						.country("country")
						.build())
			.build());
		return "Message sent";
	}

	/**
	 * 
	 */
	@GetMapping("/send/address")
	public String sendAddress(
			@RequestParam("road") String road, 
			@RequestParam("city") String city,
			@RequestParam("country") String country) {

		messageProducer.sendMessage(addressTopic, 
				Address.builder().road(road).city(city).country(country).build());
		return "Message sent";
	}
	
	/**
	 * 
	 */
	@PostMapping("/send/requestDtos")
	public RequestDto sendRequestDtos(@RequestBody RequestDto requestDto, HttpServletRequest request) {
		
		log.info("{}:{}"
				, request.getHeader("spring.application.name")
				, request.getHeader("server.port"));
		
//		requestDto.getData().forEach(System.out::println);
		requestDto.setMessage("RequestDto from Kafka-producer to " + requestDtoTopic + " topic");
		messageProducer.sendMessage(requestDtoTopic, requestDto);
		
		requestDto.setMessage("RequestDto from Kafka-producer");
		return requestDto;
	}
}