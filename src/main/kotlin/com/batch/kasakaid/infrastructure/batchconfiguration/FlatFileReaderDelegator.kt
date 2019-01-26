package com.batch.kasakaid.infrastructure.batchconfiguration

import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.core.io.ClassPathResource

interface FlatFileReaderDelegator<T> {

    fun readerName(): String

    fun resourcePath(): String

    fun columnNames(): Array<String>

    fun flatFileItemReaderBuilder(): FlatFileItemReaderBuilder<T> {
        return FlatFileItemReaderBuilder<T>()
                .name(readerName())
                .resource(ClassPathResource(resourcePath()))
                .delimited()
                .names(columnNames())
                .fieldSetMapper(object : BeanWrapperFieldSetMapper<T>() {
                    init {
                        setTargetType(entityClass())
                    }
                })
    }

    fun entityClass(): Class<T>
}