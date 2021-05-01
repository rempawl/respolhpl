package com.example.respolhpl.productDetails

import androidx.annotation.VisibleForTesting
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.example.respolhpl.BR
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.utils.event.Event

abstract class CartModel : BaseObservable() {
    abstract val addToCartCount: LiveData<Event<Int>>

    val isMinusBtnEnabled: Boolean
        @Bindable
        get() = cartQuantity > 1

    val isPlusBtnEnabled: Boolean
        @Bindable
        get() = cartQuantity < maxQuantity

    @Bindable
    var cartQuantity: Int = 0
        set(value) {
            field = if (value > maxQuantity) maxQuantity else value
            notifyPropertyChanged(BR.cartQuantity)
            notifyPropertyChanged(BR.minusBtnEnabled)
            notifyPropertyChanged(BR.plusBtnEnabled)
        }

    @Bindable
    var maxQuantity = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.plusBtnEnabled)
            notifyPropertyChanged(BR.maxQuantity)
        }

    fun onMinusBtnClick() {
        cartQuantity -= 1
    }

    fun onPlusBtnClick() {
        cartQuantity += 1
    }

    abstract fun createCartProductAndChangeQuantity(product: Product): CartProduct

    protected fun createCartProduct(product: Product) = CartProduct(
        product.id,
        price = product.price,
        quantity = cartQuantity,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )

}