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
package todo.cli;

import java.util.Map;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

/**
 * Declarative HTTP client for the To-Do microservice.
 * Annotations include RFC 6570-style URL patterns, as
 * required by Micronaut.
 */
@Client("${client.url:`http://localhost:8080`}")
public interface ToDoListClient {

	/**
	 * Fetches a page of lists.
	 *
	 * @param page Page number (starts at 0).
	 */
	@Get("/lists{?page}")
	HttpResponse<Map<?, ?>> getLists(long page);

	@Post("/lists")
	HttpResponse<String> createList(@Body String name);

	@Get("/lists/{id}")
	HttpResponse<Map<?, ?>> getList(long id);

	@Get("/lists/byName/{name}")
	HttpResponse<Map<?, ?>> getList(String name);

	/**
	 * Fetches a page of list items.
	 *
	 * @param id ID of the to-do list.
	 * @param page Page number (starts at 0).
	 */
	@Get("/lists/{id}/items{?page}")
	HttpResponse<Map<?, ?>> getListItems(long id, int page);

	/**
	 * Fetches a page of list items.
	 *
	 * @param name Name of the to-do list.
	 * @param page Page number (starts at 0).
	 */
	@Get("/lists/byName/{name}/items{?page}")
	HttpResponse<Map<?, ?>> getListItems(String name, int page);

	@Delete("/lists/{id}")
	HttpResponse<String> deleteList(long id);

	@Delete("/lists/byName/{name}")
	HttpResponse<String> deleteList(String name);

	@Put("/lists/{id}")
	HttpResponse<String> renameList(long id, @Body String name);

	@Put("/lists/byName/{oldName}")
	HttpResponse<String> renameList(String oldName, @Body String newName);

	@Post("/lists/{id}/items")
	HttpResponse<String> addItem(long id, @Body ToDoItem item);

	@Post("/lists/byName/{name}/items")
	HttpResponse<String> addItem(String name, @Body ToDoItem item);

	@Get("/items{?page}")
	HttpResponse<Map<?, ?>> getItems(int page);

	@Get("/items/{id}")
	HttpResponse<Map<?, ?>> getItem(long id);

	@Put("/items/{id}")
	HttpResponse<String> updateItem(long id, @Body ToDoItem item);

	@Delete("/items/{id}")
	HttpResponse<String> deleteItem(long id);
}
