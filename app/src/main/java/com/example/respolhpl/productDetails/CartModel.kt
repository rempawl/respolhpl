package com.example.respolhpl.productDetails

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.respolhpl.BR
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.utils.event.Event

abstract class CartModel : BaseObservable() {
    abstract val addToCartCount: LiveData<Event<Int>>

    val isMinusBtnEnabled: Boolean
        @Bindable
        get() = currentCartQuantity > 1

    val isPlusBtnEnabled: Boolean
        @Bindable
        get() = currentCartQuantity < maxQuantity


    private val _cartQuantity = MutableLiveData(0)
    val cartQuantity: LiveData<Int>
        get() = _cartQuantity

    var currentCartQuantity: Int = 1
        set(value) {
            field = if (value > maxQuantity) maxQuantity else value
            _cartQuantity.postValue(value)
            notifyPropertyChanged(BR.minusBtnEnabled)
            notifyPropertyChanged(BR.plusBtnEnabled)
        }

    @Bindable
    var maxQuantity = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.plusBtnEnabled)
            notifyPropertyChanged(BR.maxQuantity)
        }

    fun onMinusBtnClick() {
        currentCartQuantity -= 1
    }

    fun onPlusBtnClick() {
        currentCartQuantity += 1
    }

    abstract fun createCartProductAndChangeQuantity(product: Product): CartProduct

    protected fun createCartProduct(product: Product) = CartProduct(
        product.id,
        price = product.price,
        quantity = currentCartQuantity,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )

}