package com.example.respolhpl.data.model.domain


sealed class Shipping {
    abstract val cost: Double
    abstract val maxQuantity: Int

    abstract fun toShippingClassID(): Int

    //abstract fun calculateCost() : Double todo
    data class Shipping5pcsLaminat(
        override val cost: Double, override val maxQuantity: Int
    ) : Shipping() {
        override fun toShippingClassID(): Int = SHIPPING_LAMINAT_ID
    }

    data class Shipping10pcsDef(
        override val cost: Double, override val maxQuantity: Int
    ) : Shipping() {
        override fun toShippingClassID(): Int = SHIPPING_10PCS_DEF_ID
    }

    companion object {

        const val SHIPPING_LAMINAT_ID = 80
        const val SHIPPING_10PCS_DEF_ID = 73
    }
}


