package com.batch.kasakaid.infrastructure.service

import org.jooq.Field
import org.jooq.UpdatableRecord
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Timestamp

@Component
class NullValueService {

    fun populate(records: List<UpdatableRecord<*>>) {
        records.map { this.populate(it) }
    }
    private fun populate(record: UpdatableRecord<*>) {
        record.fields().forEach {
            if (it.getValue(record) != null) {
                return@forEach
            }
            val value: Any = when (it.type) {
                String::class.java -> ""
                Integer::class.java -> 0
                Long::class.java -> 0
                BigDecimal::class.java -> BigDecimal(0)
                Timestamp::class.java -> Timestamp.valueOf("9999-12-31 11:59:59")
                else -> None
            }

            if (value is None) {
                return@forEach
            }
            val t: Field<Any> = it as Field<Any>
            record.set(t, value)
        }
    }
}

class None {
    companion object
}