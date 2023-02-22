package io.billie.springkafka.config

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun hobbit(): NewTopic {
        return TopicBuilder
            .name("hobbitavro")
            .partitions(6)
            .build()
    }

    @Bean
    fun registry(): SchemaRegistryClient {
        return CachedSchemaRegistryClient("http://localhost:8081", 1000)
    }
}
