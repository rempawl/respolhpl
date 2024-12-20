package com.rempawl.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.model.domain.Images
import com.rempawl.respolhpl.data.model.domain.details.ProductType
import com.rempawl.respolhpl.data.usecase.AddToCartUseCase
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.Effect
import com.rempawl.respolhpl.utils.ProgressSemaphore
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import com.rempawl.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addToCartUseCase: AddToCartUseCase,
    private val productDetailsFormatter: ProductDetailsFormatter,
    private val productDetailsStateCase: ProductDetailsStateCase,
    progressSemaphore: ProgressSemaphore
) : BaseViewModel<ProductDetailsState, ProductDetailsEffect>(
    ProductDetailsState(),
    progressSemaphore
) {

    init {
        observeProgress(block = { isLoading -> setState { copy(showProgress = isLoading) } })
    }

    private val productId
        get() = savedStateHandle.get<Int>(KEY_PROD_ID)
            ?: throw java.lang.IllegalStateException("productId is null")
    // todo handle error

    init {
        getProduct(productId)
    }

    fun retry() {
        getProduct(productId)
    }

    fun onImageClicked() =
        setEffect(ProductDetailsEffect.NavigateToFullScreenImage(stateValue.images))

    fun onAddToCartClick() {
        with(stateValue) {
            addToCartUseCase.call(
                AddToCartUseCase.Param(
                    id = productId,
                    quantity = cartQuantity,
                    type = productType,
                    variantId = currentVariant?.id
                )
            )
                .watchProgress(progress)
                .onSuccess {
                    setEffect(ProductDetailsEffect.ItemAddedToCart(cartQuantity))
                    setState { copy(cartQuantity = 0) }
                }
                .onError { error ->
                    addError(error)
                }
                .launchIn(viewModelScope)
        }
    }

    fun onMinusBtnClick() {
        setState {
            copy(cartQuantity = cartQuantity - 1)
        }
    }

    fun onPlusBtnClick() {
        setState {
            copy(cartQuantity = cartQuantity + 1)
        }
    }

    fun onQuantityChanged(quantity: String) {
        val cartQuantity =
            if (quantity.isBlank()) 0 else calculateQuantity(
                quantity = quantity.toInt(),
                maxQuantity = stateValue.maxQuantity
            )
        setState {
            copy(cartQuantity = cartQuantity)
        }
    }

    fun onPickVariationBtnClick() {
        setState {
            copy(
                showVariantPicker = true
            )
        }
    }

    fun onCloseVariantPicker() {
        setState {
            copy(showVariantPicker = false)
        }
    }

    fun onVariantClicked(variant: VariantItem) {
        // todo stateCase
        setState {
            copy(
                currentVariant = variant,
                showVariantPicker = false,
                priceFormatted = productDetailsFormatter.formatPrice(variant.price),
                cartQuantity = calculateQuantity(
                    quantity = cartQuantity,
                    maxQuantity = variant.quantity
                )
            )
        }
    }

    fun onBuyNowClick() {
        // todo set effect
    }

    private fun getProduct(id: Int) {
        productDetailsStateCase.call(
            param = id,
            stateProvider = ::stateValue
        )
            .onEach { setState { it } }
            .launchIn(viewModelScope)
    }

    private fun calculateQuantity(quantity: Int, maxQuantity: Int): Int =
        min(quantity, maxQuantity)

    companion object {
        const val KEY_PROD_ID = "productId"
    }
}

data class ProductDetailsState(
    private val productQuantity: Int = 0,
    val productType: ProductType = ProductType.SIMPLE,
    val showProgress: Boolean = true,
    val productError: AppError? = null,
    val cartQuantity: Int = 0,
    val variants: List<VariantItem> = emptyList(),
    val currentVariant: VariantItem? = null,
    val descriptionFormatted: String = "",
    val productName: String = "",
    val toolbarLabel: String = "",
    val images: Images = Images(emptyList()),
    val showVariantPicker: Boolean = false,
    val priceFormatted: String = ""
) {
    val isSuccess: Boolean
        get() = !showProgress && productError == null

    val maxQuantity: Int
        get() = currentVariant?.quantity ?: productQuantity

    val isMinusBtnEnabled: Boolean
        get() = cartQuantity > 1

    val isPlusBtnEnabled: Boolean
        get() = cartQuantity < maxQuantity

    val isVariantCardVisible: Boolean
        get() = variants.isNotEmpty() && currentVariant != null

    val isAddToCartBtnEnabled: Boolean
        get() = cartQuantity > 0

    val isBuyNowBtnEnabled: Boolean
        get() = cartQuantity > 0
}

sealed class ProductDetailsEffect : Effect {
    data class NavigateToFullScreenImage(val images: Images) : ProductDetailsEffect()
    data class ItemAddedToCart(val quantity: Int) : ProductDetailsEffect()
    data class NavigateToCheckout(val product: CartProduct) : ProductDetailsEffect()
}
