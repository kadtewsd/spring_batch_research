package com.batch.kasakaid.application

import com.batch.kasakaid.domain.model.Person
import nu.studer.sample.tables.records.TbTrnPeopleRecord
import org.springframework.batch.item.ItemProcessor


class PersonItemProcessor : ItemProcessor<TbTrnPeopleRecord, Person> {
    override fun process(TbTrnPeopleRecord: TbTrnPeopleRecord): Person {
        val transformedPerson = Person(TbTrnPeopleRecord)
        println("Converting (${TbTrnPeopleRecord::class.java.name}) into (${transformedPerson::class.java.name})")
        return transformedPerson
    }

}