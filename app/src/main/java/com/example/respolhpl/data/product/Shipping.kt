package com.example.respolhpl.data.product


sealed class Shipping {
    abstract val cost: Double
    abstract val maxQuantity: Int

    abstract fun toShippingClassID(): Int

    //abstract fun calculateCost() : Double todo
    data class Shipping5pcsLaminat(
        override val cost: Double = 30.0, override val maxQuantity: Int = 5
    ) : Shipping() {
        override fun toShippingClassID(): Int = SHIPPING_LAMINAT_ID
    }

    data class Shipping10pcsDef(
        override val cost: Double = 11.99, override val maxQuantity: Int = 10
    ) : Shipping() {
        override fun toShippingClassID(): Int = SHIPPING_10PCS_DEF_ID
    }

    companion object {
        fun from(shippingClassID: Int): Shipping {
            return when (shippingClassID) {
                SHIPPING_LAMINAT_ID -> Shipping5pcsLaminat()
                SHIPPING_10PCS_DEF_ID -> Shipping10pcsDef()
                else -> throw IllegalArgumentException("Wrong value of shippingClassID")
            }
        }

        const val SHIPPING_LAMINAT_ID = 80
        const val SHIPPING_10PCS_DEF_ID = 73
    }
}


