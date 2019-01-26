package com.batch.kasakaid.domain.model

interface UpperCase {
    fun upper(value: String): String {
        return value.toUpperCase()
    }
}