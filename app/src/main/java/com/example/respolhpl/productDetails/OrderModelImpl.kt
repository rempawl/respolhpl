package com.example.respolhpl.productDetails

import androidx.databinding.Bindable
import javax.inject.Inject

class OrderModelImpl @Inject constructor() : OrderModel() {
    private var _quantity = 1

    @Bindable
    override var quantity: String = _quantity.toString()


}