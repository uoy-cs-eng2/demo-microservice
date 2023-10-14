package todo.microservice;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.errors.DefaultProductionExceptionHandler;
import org.apache.kafka.streams.errors.LogAndFailExceptionHandler;
import org.apache.kafka.streams.processor.DefaultPartitionGrouper;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.apache.kafka.streams.processor.internals.StreamsPartitionAssignor;
import org.apache.kafka.streams.processor.internals.assignment.HighAvailabilityTaskAssignor;
import org.rocksdb.RocksDBException;

import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;

/*
 * These annotations are to allow Kafka Streams to work from GraalVM native
 * images, as it makes significant use of reflection. Micronaut Kafka does include
 * basic GraalVM native-image configuration for regular consumers and producers,
 * but not for Kafka Streams. 
 *
 * This is not needed if we are only running the microservice from plain Java (e.g.
 * with "./gradlew run" or with a Docker image built from "./gradlew dockerBuild").
 *
 * Generally, until Kafka Streams has official GraalVM native image support, I'd
 * suggest to avoid using native images for any Micronaut microservice that uses
 * it, unless you feel adventurous and have a very good test suite :-).
 */
@SuppressWarnings("deprecation")
@TypeHint(value = {
	DefaultPartitionGrouper.class,
	DefaultProductionExceptionHandler.class,
	FailOnInvalidTimestamp.class,
	HighAvailabilityTaskAssignor.class,
	LogAndFailExceptionHandler.class,
	RocksDBException.class,
	StreamsPartitionAssignor.class,
	Serdes.ByteArraySerde.class,
	org.rocksdb.Status.class
})
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

}