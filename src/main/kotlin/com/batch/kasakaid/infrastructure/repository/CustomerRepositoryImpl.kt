package com.batch.kasakaid.infrastructure.repository

import com.batch.kasakaid.domain.model.Customer
import com.batch.kasakaid.domain.model.CustomerId
import com.batch.kasakaid.domain.model.CustomerRepository
import com.batch.kasakaid.infrastructure.repository.query.CustomerQuery
import com.google.common.collect.ImmutableList
import org.springframework.stereotype.Repository

@Repository
class CustomerRepositoryImpl(val customerQuery: CustomerQuery) : CustomerRepository {

    override fun findByCustomerIds(customerIds: ImmutableList<CustomerId>): ImmutableList<Customer> {
        return ImmutableList.copyOf(customerQuery.findById(customerIds.map { it.customerId}).map { Customer(it.customerId, it.personId) })
    }
}