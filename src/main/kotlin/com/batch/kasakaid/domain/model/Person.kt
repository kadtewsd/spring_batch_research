package com.batch.kasakaid.domain.model

import nu.studer.sample.tables.records.TbTrnPeopleRecord

/**
 * setter がないと csv ファイルを読み込む口がないので Setter を有する JOOQ のクラスを利用している
 */
data class Person(val record: TbTrnPeopleRecord) : BatchEntity(), UpperCase {
    val peopleId = PeopleId(record.personId)
    val firstName: String = record.firstName
    val lastName: String = record.lastName

    fun upperFirst(): String {
        return this.upper(firstName)
    }

    fun upperLast(): String {
        return this.upper(lastName)
    }
}
