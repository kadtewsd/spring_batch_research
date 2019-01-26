package com.batch.kasakaid.domain.model

class PeopleId(val personId: Int) {
    fun displayPerson(): String {
        return "P${personId}"
    }
}

class CustomerId(val customerId: Int) {

    fun displayCustomerId(): String {
        return "C${this.customerId}"
    }
}

class PurchaseId {
    val purchaseId: String

    constructor(purchaseId: Long) {
        this.purchaseId = "P$purchaseId"
    }

    constructor(purchaseId: String) {
        this.purchaseId = purchaseId
    }
}

class OrderId {
    val orderId: String

    constructor(orderId: Long) {
        this.orderId = "O$orderId"
    }

    constructor(orderId: String) {
        this.orderId = orderId
    }
}

