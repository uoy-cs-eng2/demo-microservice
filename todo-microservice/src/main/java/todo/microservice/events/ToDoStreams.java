/*
 * Copyright 2023 University of York
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package todo.microservice.events;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.Windowed;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Factory
public class ToDoStreams {

	@Inject
	private ItemChangeEventSerde itemChangeSerde;

	@Singleton
	@Named("list-change-metrics")
	KStream<Long, ItemChangeEvent> changeMetricsStream(ConfiguredStreamBuilder builder) {
		Properties props = builder.getConfiguration();
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "todo-streams");
		
		// Fetch all the things happening to list items
		final KStream<Long, ItemChangeEvent> itemEvents = builder
			.stream(ToDoProducer.TOPIC_ITEMS, Consumed.with(Serdes.Long(), itemChangeSerde));

		/*
		 * Re-key them by list ID, group them by list ID, and count events within
		 * 10-minute window with 1-minute grace. We don't do suppression for now.
		 */
		final KTable<Windowed<Long>, Long> table = itemEvents
			.map((k, v) -> new KeyValue<>(v.getItem().getList().getId(), "item_" + v.getType().name()))
			.groupByKey(Grouped.with(Serdes.Long(), Serdes.String()))
			.windowedBy(SlidingWindows.ofTimeDifferenceAndGrace(Duration.ofMinutes(10), Duration.ofMinutes(1)))
			.count();

		// Save the metrics
		table.toStream().to("list-change-metrics");

		return itemEvents;
	}
}
