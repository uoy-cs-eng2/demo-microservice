package todo.microservice;

import org.apache.kafka.streams.processor.DefaultPartitionGrouper;
import org.apache.kafka.streams.processor.internals.StreamsPartitionAssignor;
import org.apache.kafka.streams.processor.internals.assignment.HighAvailabilityTaskAssignor;

import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;

@SuppressWarnings("deprecation")
@TypeHint(value = {
	DefaultPartitionGrouper.class,
	HighAvailabilityTaskAssignor.class,
	StreamsPartitionAssignor.class,
})
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

}