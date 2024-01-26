package com.rempawl.respolhpl.data.model.domain.details

import android.os.Parcelable
import com.rempawl.respolhpl.data.model.domain.ProductImage
import kotlinx.parcelize.Parcelize


@androidx.annotation.Keep
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val images: List<ProductImage>,
    val thumbnailSrc: String?,
    val price: Double,
    val description: String,
    val productType: ProductType,
) : Parcelable
