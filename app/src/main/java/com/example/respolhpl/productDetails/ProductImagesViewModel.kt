package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.productDetails.ProductImagesViewModel.ProductImagesState
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import com.example.respolhpl.utils.extensions.runAsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductImagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    viewPagerPageManager: ViewPagerPageManager
) : BaseViewModel<ProductImagesState>(ProductImagesState()),
    ViewPagerPageManager by viewPagerPageManager {

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            getNavArguments()
                .onSuccess { images ->
                    setState { copy(images = images) }
                }
                .onError { setState { copy(error = it) } }
        }
    }

    private fun getNavArguments() =
        runAsResult {
            with(savedStateHandle) {
                get<Images>(KEY_IMAGES)
                    ?: throw IllegalStateException(" product id is null")
            }

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
