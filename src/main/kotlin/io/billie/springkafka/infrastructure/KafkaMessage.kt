package io.billie.springkafka.infrastructure

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

sealed interface KafkaMessage {
    fun toGenericRecord(schema: Schema): GenericRecord
    fun kafkaSubjectName(): String
}
