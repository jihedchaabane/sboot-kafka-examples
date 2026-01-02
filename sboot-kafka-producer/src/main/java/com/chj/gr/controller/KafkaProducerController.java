package com.chj.gr.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chj.gr.Address;
import com.chj.gr.Person;
import com.chj.gr.producer.MessageProducer;

@RestController
@RequestMapping("/ms6")
public class KafkaProducerController {

	@Value("${app.kafka.producer.topic}")
	private String topic;

	@Autowired
	private MessageProducer messageProducer;

	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	@GetMapping("/send/person")
	public String sendPerson(
			@RequestParam("firstName") String firstName, 
			@RequestParam("lastName") String lastName) {

		messageProducer.sendMessage(topic,
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
	 * @param message
	 * @return
	 */
	@GetMapping("/send/string")
	public String sendString(@RequestParam("message") String message) {

		messageProducer.sendMessage(topic, message);
		return "Message sent";
	}

	/**
	 * 
	 * @param road
	 * @param city
	 * @param country
	 * @return
	 */
	@GetMapping("/send/address")
	public String sendAddress(
			@RequestParam("road") String road,
			@RequestParam("city") String city,
			@RequestParam("country") String country) {

		messageProducer.sendMessage(topic, 
				Address.builder().road(road).city(city).country(country).build());
		return "Message sent";
	}
}