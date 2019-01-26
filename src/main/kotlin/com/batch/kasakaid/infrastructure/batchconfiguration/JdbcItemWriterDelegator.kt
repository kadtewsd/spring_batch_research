package com.batch.kasakaid.infrastructure.batchconfiguration

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import javax.sql.DataSource

interface JdbcItemWriterDelegator<T> {
    fun sql(): String

    fun jdbcWriterBuilder(dataSource: DataSource): JdbcBatchItemWriterBuilder<T> {
        return JdbcBatchItemWriterBuilder<T>()
                .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider<T>())
                .sql(sql())
                .dataSource(dataSource)
    }

}