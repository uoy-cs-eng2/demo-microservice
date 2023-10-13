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

import io.micronaut.configuration.kafka.serde.JsonObjectSerde;
import io.micronaut.json.JsonObjectSerializer;
import jakarta.inject.Singleton;

/**
 * Kafka Streams serde for item change events.
 */
@Singleton
public class ItemChangeEventSerde extends JsonObjectSerde<ItemChangeEvent> {

	public ItemChangeEventSerde(JsonObjectSerializer objectSerializer) {
		super(objectSerializer, ItemChangeEvent.class);
	}

}
