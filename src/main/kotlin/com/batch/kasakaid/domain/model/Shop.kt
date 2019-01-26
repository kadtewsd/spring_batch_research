package com.batch.kasakaid.domain.model

import com.batch.kasakaid.domain.model.BatchEntity

data class Shop(var shopName: String = "", var environment: String = "") : BatchEntity()
