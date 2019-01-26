package com.batch.kasakaid.application

import com.batch.kasakaid.domain.model.Shop
import nu.studer.sample.tables.records.TbTrnShopRecord
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.env.Environment


class ShopItemProcessor(val environment: Environment) : ItemProcessor<TbTrnShopRecord, Shop> {

    override fun process(shop: TbTrnShopRecord): Shop? {
        val shopName = shop.shopName.toUpperCase()

        val transformedShop = Shop(shopName, environment.getProperty("spring.datasource.driver-class-name").toString())

        println("Converting ($shop) into ($transformedShop)")

        return transformedShop
    }

}