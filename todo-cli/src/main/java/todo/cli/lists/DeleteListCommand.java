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

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import todo.cli.ToDoListClient;

@Command(name = "delete-list", description = "Deletes a list by ID or name", mixinStandardHelpOptions = true)
public class DeleteListCommand implements Runnable {

	@Inject
	ToDoListClient client;

	@Parameters(index = "0")
	private String idOrName;

	@Override
	public void run() {
		try {
			HttpResponse<String> response;
			try {
				long id = Long.parseLong(idOrName);
				response = client.deleteList(id);
			} catch (NumberFormatException ex) {
				response = client.deleteList(idOrName);
			}

			if (response.getStatus() == HttpStatus.OK) { 
				System.out.printf("Success: %s%n", response.getBody().get());
			} else {
				System.out.printf("Failure (%s): %s%n",
					response.getStatus(), response.getBody().orElse("(no text)"));
				System.exit(1);
			}
		} catch (HttpClientResponseException ex) {
			System.err.printf("Failure (%s): %s%n", ex.getStatus(), ex.getMessage());
			System.exit(1);
		}
	}

}
