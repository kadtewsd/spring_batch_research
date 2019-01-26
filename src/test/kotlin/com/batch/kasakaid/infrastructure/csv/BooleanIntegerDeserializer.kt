package com.batch.kasakaid.infrastructure.csv

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer


class BooleanIntegerDesrializer : StdDeserializer<Boolean>(Boolean::class.java) {

   override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Boolean? {
        if ("true" == p.valueAsString.toLowerCase() || "false" == p.valueAsString.toLowerCase()) return java.lang.Boolean.valueOf(p.valueAsString)
        return  "1" == p.valueAsString
    }
}