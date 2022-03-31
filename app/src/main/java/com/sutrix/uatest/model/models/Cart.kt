package com.sutrix.uatest.model.models

import kotlinx.serialization.Serializable

@Serializable
data class Cart(val items:List<Item>, val total:Double) {
    @Serializable
    data class Item(val burger:Burger, val quantity:Int)
}
