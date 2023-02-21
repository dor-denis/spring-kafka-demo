package io.billie.springkafka

import com.github.javafaker.Faker
import io.confluent.developer.avro.Hobbit
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.streams.kstream.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
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
    private val template: KafkaTemplate<Int, Hobbit>
) {
    @EventListener(ApplicationStartedEvent::class)
    fun generate() {
        val faker = Faker.instance()

        val interval = Flux.interval(Duration.ofMillis(1_000))

        val quotes = Flux.fromStream(Stream.generate { faker.hobbit().quote() })

        Flux.zip(interval, quotes).map {
            template.send(
                "hobbitavro", faker.random().nextInt(42), Hobbit(it.t2)
            )
        }.blockLast()
    }
}

@Component
class Consumer {
    @KafkaListener(topics = ["hobbitavro"], groupId = "io.billie")
    fun consume(consumerRecord: ConsumerRecord<Int, Hobbit>) {
        println("Received: ${consumerRecord.value()}, key: ${consumerRecord.key()}")
    }
}
