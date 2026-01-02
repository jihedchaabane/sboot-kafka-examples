package com.chj.gr.streams;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaTexasTopicListener {

	/** READ FROM TEXAS KAFKA TOPIC **/
	@KafkaListener(topics = "${app.kafka.consumer.dealtexassales-topic}")
	public void readRxClaimStream(@Payload String record) {

		if (record != null && record.length() > 0) {
			try {
				// DO ADDITIONAL PROCESSING WITH THIS FILTERED STREAM OF TEXAS SALES. 
				// FOR NOW JUST PRINTING IT OUT
				System.out.println("TEXAS TOPIC => " + record);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
