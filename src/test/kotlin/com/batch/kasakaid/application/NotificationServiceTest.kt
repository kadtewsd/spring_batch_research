package com.batch.kasakaid.application

import com.batch.kasakaid.BatchApplicationTest
import com.batch.kasakaid.infrastructure.batchconfiguration.NotificationConfiguration
import org.junit.Test
import org.springframework.batch.core.BatchStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NotificationServiceTest : BatchApplicationTest() {

    @Test
    fun ジョブが実行されたかを確認() {
        val jobResult = executeJob(NotificationConfiguration.JOB_NAME)
        assertEquals(1, jobResult.size)
        val execution = explorer!!.getJobExecution(jobResult[0].instanceId)
        assertNotNull(execution)
        assertEquals(execution.status, BatchStatus.COMPLETED)
    }
}
