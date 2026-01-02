package com.chj.gr.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.stereotype.Component;

@Component
public class KTableProcessor {

	// KTABLE STATE: Create a KTable for State of sales per dealer
	public void process(KStream<String, Deal> stream) {

		// Create a new KeyValue Store
		KeyValueBytesStoreSupplier dealSales = Stores.persistentKeyValueStore("dealer-sales-amount");

		KGroupedStream<String, Double> salesByDealerId = stream
				.map((key, deal) -> new KeyValue(
						deal.getDealerId(), 
						Double.parseDouble(deal.getPrice()))
				)
				.groupByKey();

		KTable<String, AggregateTotal> dealAggregate = salesByDealerId.aggregate(() -> new AggregateTotal(),
				(k, v, aggregate) -> {
					aggregate.setCount(aggregate.getCount() + 1);
					aggregate.setAmount(aggregate.getAmount() + v);
					return aggregate;
				}, Materialized.with(Serdes.String(), new AggregateTotalSerdes()));

		final KTable<String, Double> dealerTotal = dealAggregate.mapValues(value -> value.getAmount(),
				Materialized.as(dealSales));
	}
}
