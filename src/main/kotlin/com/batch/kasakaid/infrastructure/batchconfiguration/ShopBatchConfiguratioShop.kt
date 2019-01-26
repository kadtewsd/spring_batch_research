package com.batch.kasakaid.infrastructure.batchconfiguration

import com.batch.kasakaid.application.ShopItemProcessor
import com.batch.kasakaid.domain.model.Shop
import nu.studer.sample.tables.records.TbTrnShopRecord
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class ShopBatchConfiguration(val environment: Environment, val jobBuilderFactory: JobBuilderFactory, val stepBuilderFactory: StepBuilderFactory) :
        FlatFileReaderDelegator<TbTrnShopRecord>,
        JdbcItemWriterDelegator<Shop> {

    override fun readerName(): String {
        return "personItemReader"
    }

    override fun resourcePath(): String {
        return "shop-data.csv"
    }

    override fun columnNames(): Array<String> {
        return arrayOf("shopName")
    }

    override fun entityClass(): Class<TbTrnShopRecord> {
        return TbTrnShopRecord::class.java
    }

    override fun sql(): String {
        return "INSERT INTO kasakaid.tb_trn_shop (shop_name, environment) VALUES (:shopName, :environment)"
    }

    /**
     * setter is required for reading csv file. Thus, we need to use mutable record class.
     */
    @Bean
    fun shopReader(): FlatFileItemReader<TbTrnShopRecord> {
        return this.flatFileItemReaderBuilder()
                .build()
    }

    @Bean
    fun shopProcessor(): ShopItemProcessor {
        return ShopItemProcessor(this.environment)
    }

    @Bean
    fun shopWriter(dataSource: DataSource): JdbcBatchItemWriter<Shop> {
        return this.jdbcWriterBuilder(dataSource)
                .build()
    }

    @Bean
    fun importShopJob(listenerBatch: BatchJobListener, shopStep1: Step): Job {
        return jobBuilderFactory.get("importShopJob")
                .incrementer(RunIdIncrementer())
                .listener(listenerBatch)
                .flow(shopStep1)
                .end()
                .build()
    }

    @Bean
    fun shopStep1(writer: JdbcBatchItemWriter<Shop>): Step {
        return stepBuilderFactory.get("shopStep1")
                .chunk<TbTrnShopRecord, Shop>(10)
                .reader(shopReader())
                .processor(shopProcessor())
                .writer(writer)
                .build()
    }
}
