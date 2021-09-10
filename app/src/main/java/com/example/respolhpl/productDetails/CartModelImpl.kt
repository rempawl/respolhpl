package com.example.respolhpl.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.utils.event.Event

class CartModelImpl : CartModel() {
    private val _addToCartCount = MutableLiveData<Event<Int>>()
    override val addToCartCount: LiveData<Event<Int>>
        get() = _addToCartCount

    override fun createCartProductAndChangeQuantity(product: Product): CartProduct {
        _addToCartCount.value = Event(currentCartQuantity)
        val res = createCartProduct(product)
        resetCartQuantity()
        return res
    }

    private fun resetCartQuantity() {
        maxQuantity -= currentCartQuantity
        currentCartQuantity = 1
    }

}