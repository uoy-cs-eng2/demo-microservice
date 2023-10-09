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

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import todo.cli.lists.AddItemCommand;
import todo.cli.lists.CreateListCommand;
import todo.cli.lists.DeleteItemCommand;
import todo.cli.lists.DeleteListCommand;
import todo.cli.lists.FetchItemCommand;
import todo.cli.lists.FetchItemsCommand;
import todo.cli.lists.FetchListCommand;
import todo.cli.lists.FetchListItemsCommand;
import todo.cli.lists.FetchListsCommand;
import todo.cli.lists.RenameListCommand;
import todo.cli.lists.UpdateItemCommand;

@Command(
	name = "todo-cli",
	description = "Manages lists",
	mixinStandardHelpOptions = true,
	subcommands = {
		AddItemCommand.class,
		CreateListCommand.class,
		FetchItemCommand.class,
		FetchItemsCommand.class,
		FetchListCommand.class,
		FetchListsCommand.class,
		FetchListItemsCommand.class,
		DeleteItemCommand.class,
		DeleteListCommand.class,
		RenameListCommand.class,
		UpdateItemCommand.class
	}
)
public class TodoCli implements Runnable {

	public static void main(String[] args) throws Exception {
        PicocliRunner.run(TodoCli.class, args);
    }

	@Override
    public void run() {
        System.out.println(
        	"Please use the subcommands to manage lists: "
        	+ "they can be listed by passing the --help option.");
    }

}
