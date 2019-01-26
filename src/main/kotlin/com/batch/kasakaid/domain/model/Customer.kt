package com.batch.kasakaid.domain.model

class Customer : BatchEntity {
    private val customerId: CustomerId
    private val peopleId: PeopleId

    constructor(argCustomerId: CustomerId, argPerson: Person) {
        customerId = argCustomerId
        peopleId = argPerson.peopleId
    }

    constructor(argCustomerId: Int, argPeopleId: Int) {
        customerId = CustomerId(argCustomerId)
        peopleId = PeopleId(argPeopleId)
    }
}