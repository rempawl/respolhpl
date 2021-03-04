package com.example.respolhpl.productDetails

import androidx.databinding.BaseObservable

abstract class OrderModel : BaseObservable() {
    abstract var quantity: String
}