package com.batch.kasakaid.infrastructure.csv

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate


class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {

    private val localDateTimeStringConverter: LocalDateTimeDeserializer

    init {
        this.localDateTimeStringConverter = LocalDateTimeDeserializer()
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate? {
        val result = localDateTimeStringConverter.deserialize(p, ctxt)
        return result?.toLocalDate()
    }
}