package com.batch.kasakaid.infrastructure


import com.batch.kasakaid.infrastructure.csv.InstanceCreator
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.springframework.context.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader

interface DatabaseCSVVerification {

    fun <T> getList(sourceDirectory: String, applicationContext: ApplicationContext, clz: Class<T>, csvSchema: CsvSchema): List<T> {
        val mapper = InstanceCreator.csvObjectMapper()
        val reader = mapper.readerFor(clz).with(csvSchema.withHeader())
        val mapps: MappingIterator<T> = reader.readValues(toCsv(sourceDirectory, applicationContext, clz))
        println(clz.name)
        return mapps.readAll()
    }

    fun toCsv(sourceDataDirectory: String, applicationContext: ApplicationContext, clz: Class<*>): String {
        val reader = BufferedReader(InputStreamReader(
                applicationContext.getResource("classpath:test_data/" + sourceDataDirectory + "/" + csvFileName(clz)).inputStream
        ))
        val stb = StringBuilder()
        var line: String
        do {
            line = reader.readLine()
            if (line == null) {
                break
            }
            stb.append(line)
            stb.append("\n")
        } while (true)
        reader.close()
        return stb.toString().trim { it <= ' ' }
    }

    fun csvFileName(clz: Class<*>): String {
        return clz.simpleName.replace("Test", "").replace("Paas", "") + ".csv"
    }
}