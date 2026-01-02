package com.chj.gr.streams;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KStreamProcessor {

	@Value("${app.kafka.consumer.dealtexassales-topic}")
	private String dealTexasSalesTopic;

	public void process(KStream<String, Deal> stream) {

		// KSTREAM FILTER: Filter the Stream to get Texas sales into a new Texas Topic
		stream.filter(new Predicate<String, Deal>() {
			@Override
			public boolean test(String key, Deal object) {
				if (object != null 
						&& object.getState() != null
						&& object.getState().trim().equalsIgnoreCase("TEXAS")) {
					return true;
				} else {
					return false;
				}
			}
		}).to(dealTexasSalesTopic);

	}
}
