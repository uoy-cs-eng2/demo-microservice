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

import io.micronaut.serde.annotation.Serdeable;
import todo.microservice.domain.ToDoItem;

@Serdeable
public class ItemChangeEvent {

	private ChangeType type;
	private ToDoItem item;

	public ItemChangeEvent() {
		// no-op
	}

	public ItemChangeEvent(ChangeType type, ToDoItem item) {
		this.type = type;
		this.item = item;
	}

	public ChangeType getType() {
		return type;
	}

	public void setType(ChangeType type) {
		this.type = type;
	}

	public ToDoItem getItem() {
		return item;
	}

	public void setItem(ToDoItem item) {
		this.item = item;
	}

}
