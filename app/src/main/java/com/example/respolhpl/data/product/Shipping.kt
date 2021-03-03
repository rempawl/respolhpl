package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.remote.ShippingRemote


data class Shipping(
    val cost: Double,
    val maxQuantity: Int
) {
    companion object {
        fun from(remote: ShippingRemote): Shipping {
            return Shipping(cost = remote.cost, maxQuantity = remote.maxQuantity)
        }
    }
}


