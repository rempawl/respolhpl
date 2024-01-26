package com.rempawl.respolhpl.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutArgs(
    val product: BuyNowProduct? = null
) : Parcelable

@Parcelize
data class BuyNowProduct(
    val id: Int,
    val quantity: Int
) : Parcelable