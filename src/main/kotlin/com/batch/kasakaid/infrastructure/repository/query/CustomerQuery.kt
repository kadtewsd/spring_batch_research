package com.batch.kasakaid.infrastructure.repository.query

import com.batch.kasakaid.infrastructure.service.NullValueService
import nu.studer.sample.tables.TbTrnCustomer
import nu.studer.sample.tables.records.TbTrnCustomerRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Component

@Component
class CustomerQuery(val dsl: DSLContext, val service: NullValueService) {

    fun findById(customerIds: List<Int>): Result<TbTrnCustomerRecord> {
        val result = dsl
                .selectFrom(TbTrnCustomer.TB_TRN_CUSTOMER)
                .where(TbTrnCustomer.TB_TRN_CUSTOMER.CUSTOMER_ID.`in`(customerIds))
                .orderBy(TbTrnCustomer.TB_TRN_CUSTOMER.CUSTOMER_ID, TbTrnCustomer.TB_TRN_CUSTOMER.CUSTOMER_ID)
                .fetch()
        service.populate(result)
        return result
    }
}
