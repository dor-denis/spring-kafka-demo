package io.billie.springkafka.config

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
}
