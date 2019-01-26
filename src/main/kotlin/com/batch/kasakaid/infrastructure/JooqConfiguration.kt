package com.batch.kasakaid.infrastructure

import org.jooq.DSLContext
import org.jooq.conf.Settings
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JooqConfiguration(val dataSource: DataSource) {

    @Bean
    fun connectionProvider(): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(
                TransactionAwareDataSourceProxy(this.dataSource)
        )
    }

    @Bean
    fun dsl(): DSLContext {
        return DefaultDSLContext(configuration())
    }

    @Bean
    fun configuration(): DefaultConfiguration {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider())
        jooqConfiguration.setSettings(jooqSettings())
//        jooqConfiguration.set(DefaultExecuteListenerProvider(exeptionTransformer()))
        return jooqConfiguration
    }

    fun jooqSettings(): Settings {
         return Settings()
                .withExecuteLogging(true)
    }
}