package com.rempawl.respolhpl.data.model.domain

import android.os.Parcelable
import com.rempawl.respolhpl.data.model.domain.details.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val quantity: Int,
    val variantId : Int?
) : Parcelable
