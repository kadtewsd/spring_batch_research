package com.batch.kasakaid

import com.batch.kasakaid.infrastructure.TestConfig
import com.batch.kasakaid.infrastructure.TestResource
import com.batch.kasakaid.infrastructure.helper.FileLoadHelper
import org.h2.tools.RunScript
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.batch.core.JobInstance
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BatchApplication::class, TestConfig::class])
abstract class BatchApplicationTest {

    @Before
    fun createTables() {
        val dbConn = testResource!!.openDbConn()
        if (!testResource!!.initialized()) {
            RunScript.execute(dbConn.connection, fileLoadHelper!!.asReader("create_schema.sql"))
        }
    }

    @Autowired
    var testConfig: TestConfig? = null

    @Autowired
    var testResource: TestResource? = null

    @Autowired
    var fileLoadHelper: FileLoadHelper? = null

    @Autowired
    var context: ApplicationContext? = null


    @After
    fun tearDown() {
        testResource!!.truncateTable()
        testResource!!.deleteMasterData()
        val dbConn = testResource!!.openDbConn()
    }

    @Autowired
    var explorer: JobExplorer? = null

    fun executeJob(jobName: String, vararg args: String = arrayOf("")): List<JobInstance> {
        val candidate = context!!.getBeansOfType(JobLauncherCommandLineRunner::class.java).values
        val runner = candidate.first()
        testResource!!.insertMasterData()
        runner.setJobNames(jobName)
        runner.run(*args)
        return this.explorer!!.getJobInstances(jobName, 0, 100)
    }

    companion object {
        init {
            //spring のプロファイルを test にしてテスト環境の設定を読み込むことができるようにする。
            System.setProperty("spring.profiles.active", "test")
            // batch.job.enabled を true にすると全てのジョブを実行しようとする。
            // batch.job.enabled を false にすると context にCommandLineRunner が入らない。
            // システム・プロパティで spring.batch.job.names で実行すると、最初のテストは 目的のテストが動くが
            // 連続でテストすると setProperty したところですでに Spring の情報は出来上がっているので、最初のジョブがまた動く。
            // そのため、Boot 起動時はジョブは動かさず、一方で、CommandLineRunner はBean 化させておく。
            // Boot 起動後に、手動で目的のジョブを起動する。
            System.setProperty("spring.batch.job.names", "NO_JOBS_ARE_EXECUTED")
        }
    }
}

