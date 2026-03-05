package com.siotman.vote.core.common.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.victools.jsonschema.generator.OptionPreset
import com.github.victools.jsonschema.generator.SchemaGenerator
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder
import com.github.victools.jsonschema.generator.SchemaVersion
import com.github.victools.jsonschema.module.jackson.JacksonModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object CoreJson {
    const val DEFAULT_TIME_ZONE_ID: String = "Asia/Seoul"
    const val LOCAL_DATE_TIME_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss"

    val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)

    val javaTimeModule: JavaTimeModule = JavaTimeModule().apply {
        addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(localDateTimeFormatter))
        addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(localDateTimeFormatter))
    }

    val objectMapper: ObjectMapper = jacksonObjectMapper()
        .registerModule(javaTimeModule)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE_ID))

    private val schemaGenerator: SchemaGenerator by lazy {
        val configBuilder = SchemaGeneratorConfigBuilder(
            objectMapper,
            SchemaVersion.DRAFT_2020_12,
            OptionPreset.PLAIN_JSON,
        )
        configBuilder.with(JacksonModule())
        SchemaGenerator(configBuilder.build())
    }

    fun jsonSchema(type: Class<*>): String {
        val schema = schemaGenerator.generateSchema(type)
        return objectMapper.writeValueAsString(schema)
    }
}
