package com.example.respolhpl.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted id: Long,
) : ViewModel() {


    @AssistedFactory
    interface ProductDetailsViewModelFactory {
        fun create(id: Long): ProductDetailsViewModel
    }

    companion object {

    }
}