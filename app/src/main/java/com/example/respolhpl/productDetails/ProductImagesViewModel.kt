package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.productDetails.ProductImagesViewModel.ProductImagesState
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.DefaultError
import com.example.respolhpl.utils.NoEffects
import com.example.respolhpl.utils.extensions.EitherResult
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductImagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    viewPagerPageManager: ViewPagerPageManager
) : BaseViewModel<ProductImagesState,NoEffects>(ProductImagesState()),
    ViewPagerPageManager by viewPagerPageManager {

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            getImagesFromNavArg()
                .onSuccess { images ->
                    println(images)
                    setState { copy(images = images, error = null) }
                }
                .onError {
                    println(it)
                    setState { copy(error = it) }
                }
        }
    }

    private fun getImagesFromNavArg(): EitherResult<Images> = either {
        savedStateHandle.get<Images>(KEY_IMAGES) ?: raise(NullProductIdError)
    }


    fun retry() {
        getImages()
    }

    data class ProductImagesState(
        val error: DefaultError? = null,
        val images: Images = Images(emptyList())
    )

    companion object {
        const val KEY_IMAGES = "images"
    }
}

object NullProductIdError : DefaultError()
