package com.rempawl.respolhpl.home

import com.rempawl.respolhpl.list.BaseListItem
import javax.annotation.concurrent.Immutable

@Immutable
data class ProductMinimalListItem(
    val productId: Int,
    val name: String,
    val thumbnailSrc: String?,
    val priceFormatted: String,
    override val itemId: Int = productId
) : BaseListItem