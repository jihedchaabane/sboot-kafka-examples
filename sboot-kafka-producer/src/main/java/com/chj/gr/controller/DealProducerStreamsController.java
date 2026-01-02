package com.chj.gr.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chj.gr.producer.MessageProducer;
import com.chj.gr.streams.Deal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/ms6/topics/bytype")
public class DealProducerStreamsController {

	@Value("${app.kafka.producer.deal-topic}")
	private String dealTopic;

	@Autowired
	private MessageProducer messageProducer;
	
	private ObjectMapper mapper = new ObjectMapper();
	/**
	 * https://medium.com/@tobintom/introducing-kafka-streams-with-spring-boot-be1d6f7f3b76
	 */
	@GetMapping("/send/deals")
	public String sendDeals() {
		
		File file = new File(System.getProperty("user.dir").concat("/src/main/resources/deals/deals.json"));

		try {
			List<Deal> list = mapper.readValue(file,  new TypeReference<List<Deal>>() {});
			list.stream().forEach(deal -> messageProducer.sendMessage(dealTopic, deal));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Deals sent";
	}
}