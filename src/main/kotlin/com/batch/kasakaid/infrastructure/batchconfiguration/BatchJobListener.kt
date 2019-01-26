package com.batch.kasakaid.infrastructure.batchconfiguration

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component


@Component
class BatchJobListener : JobExecutionListenerSupport() {

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("!!! ${jobExecution.jobConfigurationName} starts now!")
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status === BatchStatus.COMPLETED) {
            val startTime = jobExecution.startTime.time
            val endTime = jobExecution.endTime.time
            log.info("!!! ${jobExecution.jobConfigurationName} FINISHED! Elapsed time was ${endTime - startTime}")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(BatchJobListener::class.java)
    }
}