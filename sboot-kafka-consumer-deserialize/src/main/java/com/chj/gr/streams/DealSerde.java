package com.chj.gr.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class DealSerde extends Serdes.WrapperSerde<Deal> {
	public DealSerde() {
		super(new JsonSerializer<>(), new JsonDeserializer<>(Deal.class));
	}
}
