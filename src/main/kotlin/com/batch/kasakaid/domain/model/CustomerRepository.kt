package com.batch.kasakaid.domain.model

import com.google.common.collect.ImmutableList

interface CustomerRepository  {
    fun findByCustomerIds(customerIds: ImmutableList<CustomerId>): ImmutableList<Customer>
}
