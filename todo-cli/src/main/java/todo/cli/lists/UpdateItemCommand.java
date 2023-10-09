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

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import todo.cli.ToDoItem;
import todo.cli.ToDoListClient;

@Command(name = "update-item", description = "Updates an item", mixinStandardHelpOptions = true)
public class UpdateItemCommand implements Runnable {

	@Inject
	ToDoListClient client;

	@Parameters(index = "0", description = "ID of the item (e.g. /items/ID)")
	private long itemId;

	@ArgGroup(multiplicity = "1", exclusive = false, heading = "PROVIDING NEW VALUES\n", headingKey = "updates")
	UpdateSection updates;

	static class UpdateSection {
		@Option(names = { "-l",	"--list-id" }, paramLabel = "ID", description = "Changes the list to which the item belongs")
		private Long listId;

		@Option(names = { "-s",	"--timestamp" }, paramLabel = "TIMESTAMP", description = "Changes the timestamp of the item, which must be in ISO-8601 format (e.g. 'YYYY-MM-DD' followed by the 'T' character and then 'HH:MM:SS', without quotes).")
		private LocalDateTime timestamp;

		@Option(names = { "-b",	"--body-file" }, paramLabel = "PATH", description = "Changes the body of the message to the contents of the specified file")
		private Path bodyPath;

		@Option(names = { "-t", "--title" }, paramLabel = "TITLE", description = "Changes the title of the item")
		private String title;
	}

	@Override
	public void run() {
		try {
			// Create JSON bean to be sent
			ToDoItem item = new ToDoItem();
			if (updates.listId != null) {
				item.setListId(updates.listId);
			}
			if (updates.title != null) {
				item.setTitle(updates.title);
			}
			if (updates.bodyPath != null) {
				item.setBody(Files.readString(updates.bodyPath));
			}
			if (updates.timestamp != null) {
				item.setTimestamp(updates.timestamp);
			}

			HttpResponse<String> response = client.updateItem(itemId, item);
			if (response.getStatus() == HttpStatus.OK) {
				System.out.printf("Success: %s%n", response.getBody().get());
			} else {
				System.err.printf("Failure (%s): %s%n",
					response.getStatus(), response.getBody().orElse("(no text)"));
				System.exit(1);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (HttpClientResponseException ex) {
			System.err.printf("Failure (%s): %s%n", ex.getStatus(), ex.getMessage());
			System.exit(1);
		}
	}

}
