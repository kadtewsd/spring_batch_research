package com.batch.kasakaid.infrastructure.helper

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

@Component
class FileLoadHelper(val applicationContext: ApplicationContext) {

    fun asReader(filePath: String): Reader {
        return BufferedReader(InputStreamReader(
                applicationContext.getResource("classpath:$filePath").inputStream))
    }
}
