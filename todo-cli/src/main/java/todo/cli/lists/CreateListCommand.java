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

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import todo.cli.ToDoListClient;

@Command(name = "create-list", description = "Creates a list", mixinStandardHelpOptions = true)
public class CreateListCommand implements Runnable {

	@Inject
	ToDoListClient client;

	@Parameters(index = "0")
	private String name;

	@Override
	public void run() {
		try {
			HttpResponse<String> response = client.createList(name);
			if (response.getStatus() == HttpStatus.CREATED) {
				System.out.printf("List created successfully at %s%n",
					response.getHeaders().get(HttpHeaders.LOCATION));
			} else {
				System.err.printf("Failed to create list called '%s': %s%n",
					name, response.getBody().orElse("(no text)"));
				System.exit(1);
			}
		} catch (HttpClientResponseException ex) {
			System.err.printf("Failed (%s): %s%n",
				ex.getStatus(), ex.getMessage());
			System.exit(1);
		}
	}

}
