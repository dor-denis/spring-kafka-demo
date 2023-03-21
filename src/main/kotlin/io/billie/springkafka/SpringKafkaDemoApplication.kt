package io.billie.springkafka

import com.github.javafaker.Faker
import com.ozean12.kafkaavrolib.KafkaAvroPublisher
import io.billie.springkafka.infrastructure.HobbitMessage
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.streams.kstream.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.Stream

@SpringBootApplication
class SpringKafkaDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringKafkaDemoApplication>(*args)
}

@Component
class Producer(
    private val kafkaAvroPublisher: KafkaAvroPublisher
) {
    @EventListener(ApplicationStartedEvent::class)
    fun generate() {
        val faker = Faker.instance()

        val interval = Flux.interval(Duration.ofMillis(1_000))

        val quotes = Flux.fromStream(Stream.generate { faker.hobbit().quote() })

        Flux.zip(interval, quotes).map {
            kafkaAvroPublisher.send(
                "hobbitavro", faker.random().nextInt(42).toString(), HobbitMessage(faker.hobbit().quote())
            )
        }.blockLast()
    }
}

@Component
class Consumer {
    @KafkaListener(topics = ["hobbitavro"], groupId = "io.billie")
    fun consume(consumerRecord: ConsumerRecord<Int, GenericRecord>) {
        println("Received: ${consumerRecord.value()}, key: ${consumerRecord.key()}")
    }
}
