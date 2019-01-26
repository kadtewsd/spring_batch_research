package com.batch.kasakaid.infrastructure.csv

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
object InstanceCreator {

    fun csvObjectMapper(): CsvMapper {
        val timeModule = JavaTimeModule()
        timeModule.addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        timeModule.addDeserializer(LocalDate::class.java, LocalDateDeserializer())
        timeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        timeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())

        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(String::class.java, StringNullDesrializer())
        simpleModule.addDeserializer(Boolean::class.java, BooleanIntegerDesrializer())
        simpleModule.addDeserializer(Boolean::class.javaPrimitiveType, BooleanIntegerDesrializer())
        return CsvMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .registerModule(timeModule)
                .registerModule(simpleModule) as CsvMapper
    }
}