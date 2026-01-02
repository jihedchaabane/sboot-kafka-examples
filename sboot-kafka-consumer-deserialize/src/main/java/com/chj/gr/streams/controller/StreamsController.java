package com.chj.gr.streams.controller;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toyotasales")
public class StreamsController {

	private final StreamsBuilderFactoryBean factoryBean;

	public StreamsController(StreamsBuilderFactoryBean factoryBean) {
		this.factoryBean = factoryBean;
	}

	/** READ FROM THE KTABLE DEALER STATE **/
	@GetMapping("/dealer/{id}")
	public String getDealerSales(@PathVariable String id) {
		
		KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
		
		// Read the KTable Store and get the total aggregated sales by dealer id
		ReadOnlyKeyValueStore<String, Long> amounts = kafkaStreams.store(
				StoreQueryParameters.fromNameAndType("dealer-sales-amount", QueryableStoreTypes.keyValueStore()));
		
		return "Total Car Sales for Dealer " + id + " is $" + amounts.get(id);
	}

}
