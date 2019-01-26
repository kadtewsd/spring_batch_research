package com.batch.kasakaid.infrastructure.batchconfiguration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.Tasklet

interface TaskletStepbuilder {

    val stepBuilderFactory: StepBuilderFactory

    fun buildStep(stepName: String, tasklet: Tasklet): Step {
        return stepBuilderFactory.get(stepName)
                .tasklet(tasklet)
                .build()
    }
}