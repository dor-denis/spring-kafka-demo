package io.billie.springkafka.infrastructure

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord

class HobbitMessage(val quote: String, val name: String): KafkaMessage {
    override fun toGenericRecord(schema: Schema): GenericRecord {
        val record = GenericData.Record(schema)
        record.put("quote", quote)
        record.put("name", name)

        return record
    }

    override fun kafkaSubjectName(): String = "hobbitavro-value"
}
