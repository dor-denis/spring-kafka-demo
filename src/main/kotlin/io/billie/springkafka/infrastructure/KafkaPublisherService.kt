package io.billie.springkafka.infrastructure

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaPublisherService(
    val kafkaTemplate: KafkaTemplate<String, GenericRecord>,
    val registryClient: SchemaRegistryClient
) {
    fun send(topic: String, key: String, message: KafkaMessage) {
        val parser = Schema.Parser()

        val schemas = registryClient.getSchemas(message.kafkaSubjectName(), false, true)
        assert(schemas.count() == 1)

        val schema: Schema = parser.parse(schemas[0].toString())

        kafkaTemplate.send(topic, key, message.toGenericRecord(schema))
    }
}
