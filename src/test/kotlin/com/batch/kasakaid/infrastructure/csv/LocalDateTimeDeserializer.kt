package com.batch.kasakaid.infrastructure.csv

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime? {
        return if ("null" == p.getValueAsString()) null else LocalDateTime.parse(p.getValueAsString(), parser)
    }

    companion object {

        private val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}