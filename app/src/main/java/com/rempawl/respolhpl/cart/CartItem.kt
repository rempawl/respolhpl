package com.rempawl.respolhpl.cart

import com.rempawl.respolhpl.list.BaseListItem
import com.rempawl.respolhpl.list.UniqueListItem

sealed class CartItem : BaseListItem {
    data class Product(
        private val price : Double,
        val quantityFormatted: String,
        val id: Int,
        val cost: String,
        val priceFormatted: String,
        val thumbnailSrc: String?,
        val name: String,
        override val itemId: Any = id
    ) : CartItem(), BaseListItem

    data class Summary(
        val cost: String,
    ) : CartItem(), BaseListItem by UniqueListItem()
}