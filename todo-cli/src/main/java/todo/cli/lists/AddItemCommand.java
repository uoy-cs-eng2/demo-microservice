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
package todo.cli.lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import todo.cli.ToDoItem;
import todo.cli.ToDoListClient;

@Command(name = "add-item", description = "Adds an item to a list", mixinStandardHelpOptions = true)
public class AddItemCommand implements Runnable {

	@Inject
	ToDoListClient client;

	@Parameters(index = "0")
	private String listIdOrName;

	@Parameters(index = "1")
	private String title;

	@Parameters(index = "2")
	private Path bodyPath;

	@Override
	public void run() {
		try {
			// Create JSON bean to be sent
			ToDoItem item = new ToDoItem();
			item.setTimestamp(LocalDateTime.now());
			item.setTitle(title);
			item.setBody(Files.readString(bodyPath));

			HttpResponse<String> response;
			try {
				response = client.addItem(Long.parseLong(listIdOrName), item);
			} catch (NumberFormatException ex) {
				response = client.addItem(listIdOrName, item);
			}

			// NOTE: declarative HttpClient will throw exceptions for statuses >= 400, *except* for 404
			if (response.getStatus() == HttpStatus.CREATED) {
				System.out.printf("Item added successfully to list %s at %s%n", listIdOrName,
					response.getHeaders().get(HttpHeaders.LOCATION));
			} else {
				System.err.printf("Failed to add item to list %s: %s%n",
					listIdOrName, response.getBody().orElse("(no text)"));
				System.exit(1);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (HttpClientResponseException ex) {
			System.err.printf("Failed (%s): %s%n", ex.getStatus(), ex.getMessage());
			System.exit(1);
		}
	}

}
