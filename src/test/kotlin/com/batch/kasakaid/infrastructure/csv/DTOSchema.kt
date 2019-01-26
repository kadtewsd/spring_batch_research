package com.batch.kasakaid.infrastructure.csv

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext


class DTOSchema {

    @Autowired
    protected var applicationContext: ApplicationContext? = null

    @Autowired
    protected var objectMapper: ObjectMapper? = null

    fun toJson(dto: Any): String {
        return objectMapper!!.writeValueAsString(dto)
    }
}