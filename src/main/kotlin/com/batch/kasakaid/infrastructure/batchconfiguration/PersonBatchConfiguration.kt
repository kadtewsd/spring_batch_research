package com.batch.kasakaid.infrastructure.batchconfiguration

import com.batch.kasakaid.application.PersonItemProcessor
import com.batch.kasakaid.domain.model.Person
import nu.studer.sample.tables.records.TbTrnPeopleRecord
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class PeopleBatchConfiguration(val jobBuilderFactory: JobBuilderFactory, val stepBuilderFactory: StepBuilderFactory, val listener: JobExecutionListener) :
        FlatFileReaderDelegator<TbTrnPeopleRecord>,
        JdbcItemWriterDelegator<Person> {

    override fun readerName(): String {
        return "personItemReader"
    }

    override fun resourcePath(): String {
        return "customer-data.csv"
    }

    override fun columnNames(): Array<String> {
        return arrayOf("firstName", "lastName")
    }

    override fun entityClass(): Class<TbTrnPeopleRecord> {
        return TbTrnPeopleRecord::class.java
    }

    @Bean
    fun userReader(): FlatFileItemReader<TbTrnPeopleRecord> {
        return this.flatFileItemReaderBuilder()
                .build()
    }

    @Bean
    fun userProcessor(): PersonItemProcessor {
        return PersonItemProcessor()
    }

    override fun sql(): String {
        return "INSERT INTO kasakaid.tb_trn_people (first_name, last_name) VALUES (:firstName, :lastName)"
    }

    @Bean
    fun userWriter(dataSource: DataSource): JdbcBatchItemWriter<Person> {
        return this.jdbcWriterBuilder(dataSource)
                .build()
    }

    @Bean
    fun importUserJob(personStep1: Step): Job {
        return jobBuilderFactory.get("importPeopleJob")
                .incrementer(RunIdIncrementer())
                .listener(listener)
                .flow(personStep1)
                .end()
                .build()
    }

    @Bean
    fun personStep1(userWriter: JdbcBatchItemWriter<Person>): Step {
        return stepBuilderFactory.get("personStep1")
                .chunk<TbTrnPeopleRecord, Person>(10)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter)
                .build()
    }
}
