package com.batch.kasakaid.infrastructure.batchconfiguration

import com.batch.kasakaid.application.NotificationService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class NotificationConfiguration(val jobBuilderFactory: JobBuilderFactory, override val stepBuilderFactory: StepBuilderFactory, val jobListener: BatchJobListener, val addressChangeNotificationService: NotificationService) : TaskletStepbuilder {

    @Bean
    fun selectStep(): Step {
        return this.buildStep(JOB_NAME, addressChangeNotificationService)
    }

    @Bean
    fun notification(selectStep: Step): Job {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(RunIdIncrementer())
                .listener(jobListener)
                .start(selectStep)
                .build()
    }

    companion object {
       const val JOB_NAME = "notification"
    }
}