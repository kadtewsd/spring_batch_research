package com.batch.kasakaid

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FirstTest : BatchApplicationTest() {

    @Test
    fun 環境でプロパティファイルを読み込み分けられるか() {
        assertNotNull(testConfig)
        assertEquals("never", testConfig!!.environment.getProperty("spring.batch.initialize-schema"), "spring batch の設定")
        assertEquals("true", testConfig!!.environment.getProperty("spring.batch.job.enabled"))
        assertEquals("net.sf.log4jdbc.DriverSpy", testConfig!!.environment.getProperty("spring.datasource.driver-class-name"))
        assertEquals("test", testConfig!!.environment.getProperty("spring.profiles.active"))
    }

    @Test
    fun マスターデータを投入できるかをテスト() {
        testResource!!.insertMasterData()
        assertTrue { true }
    }
}
