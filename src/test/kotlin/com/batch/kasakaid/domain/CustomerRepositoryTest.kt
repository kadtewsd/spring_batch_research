package com.batch.kasakaid.domain

import com.batch.kasakaid.BatchApplicationTest
import com.batch.kasakaid.domain.model.CustomerId
import com.batch.kasakaid.domain.model.CustomerRepository
import com.google.common.collect.ImmutableList
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class CustomerRepositoryTest : BatchApplicationTest() {

    @Autowired
    lateinit var repository: CustomerRepository

    val CSV_RESOURCE_PATH = "first_test"

    @Test
    fun 顧客が取得できるかをチェック() {
        testResource!!.insertData(CSV_RESOURCE_PATH)
        val results =  repository.findByCustomerIds(ImmutableList.of(
                CustomerId(1),
                CustomerId(2)
                )
        )
        assertEquals(2, results.size)
    }
}