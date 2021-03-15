package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.remote.ShippingRemote


data class Shipping(
    val cost: Double,
    val maxQuantity: Int
) {

    //    data class Shipping5pcsGLS(override val cost: Double = 30.0,
//                            override val maxQuantity: Int = 5
//    ) : Shipping()
//    data class Shipping10pcsGLS(override val cost: Double = 11.99, override val maxQuantity: Int = 10) : Shipping()
//    todo shipping types
    companion object {
        fun from(remote: ShippingRemote): Shipping {
            return Shipping(cost = remote.cost, maxQuantity = remote.maxQuantity)
        }
    }
}


