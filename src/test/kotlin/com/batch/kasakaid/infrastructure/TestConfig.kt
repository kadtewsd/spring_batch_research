package com.batch.kasakaid.infrastructure

import org.jooq.DSLContext
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType


@Configuration
@ComponentScan(basePackageClasses = [TestConfig::class])
@Profile("test") // プロファイルを設定しないとプロダクションが動いた時も読み込まれる
class TestConfig(val environment: Environment) {

    @Bean
    fun dataSource(): TransactionAwareDataSourceProxy {
        if (environment.getProperty("spring.datasource.url")!!.contains("mem")) {
            val ds = EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                    .addScript("create_schema.sql") このタイミングでスキーマを作ると flyway がテーブルを検証して、そんなもの history テーブルにない、と怒る。
                    .build()
            return TransactionAwareDataSourceProxy(ds)
        }
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name")!!.toString())
        dataSource.url = environment.getProperty("spring.datasource.url")
        dataSource.username = environment.getProperty("spring.datasource.username")
        dataSource.password = environment.getProperty("spring.datasource.password")
        return TransactionAwareDataSourceProxy(dataSource)
    }

    @Bean
    fun connectionProvider(): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(this.dataSource())
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
        return jooqConfiguration
    }

    fun jooqSettings(): Settings {
        return Settings()
                .withExecuteLogging(true)
                .withRenderSchema(false)
                .withRenderNameStyle(RenderNameStyle.AS_IS)
    }
}
