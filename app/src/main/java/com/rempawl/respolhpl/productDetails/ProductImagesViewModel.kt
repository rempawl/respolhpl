package com.rempawl.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import com.rempawl.respolhpl.data.model.domain.Images
import com.rempawl.respolhpl.productDetails.ProductImagesViewModel.ProductImagesState
import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.NoEffects
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import com.rempawl.respolhpl.utils.extensions.toEitherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductImagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    viewPagerPageManager: ViewPagerPageManager
) : BaseViewModel<ProductImagesState, NoEffects>(ProductImagesState()),
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

    private fun getImagesFromNavArg(): EitherResult<Images> = catch(
        block = { savedStateHandle.get<Images>(KEY_IMAGES)!!.right() },
        catch = { throwable ->
            when (throwable) {
                is NullPointerException -> NullProductIdError.left()
                else -> throwable.toEitherResult()
            }
        }
    )

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
