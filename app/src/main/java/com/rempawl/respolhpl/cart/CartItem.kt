package com.rempawl.respolhpl.cart

import com.rempawl.respolhpl.list.BaseListItem
import com.rempawl.respolhpl.list.UniqueListItem

sealed class CartItem : BaseListItem {
    data class Product(
        val quantity: String,
        val id: Int,
        val price: String,
        val thumbnailSrc: String?,
        val name: String,
        override val itemId: Any = id
    ) : CartItem(), BaseListItem

    data class Summary(
        val cost: String,
    ) : CartItem(), BaseListItem by UniqueListItem()
}