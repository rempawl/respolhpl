package com.rempawl.respolhpl.cart

import androidx.lifecycle.viewModelScope
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects
import com.rempawl.respolhpl.cart.CartViewModel.CartState
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.usecase.ClearCartUseCase
import com.rempawl.respolhpl.data.usecase.GetCartProductsUseCase
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.Effect
import com.rempawl.respolhpl.utils.extensions.addItemIf
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import com.rempawl.respolhpl.utils.log
import com.rempawl.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val cartFormatter: CartFormatter,
    private val clearCartUseCase: ClearCartUseCase
) : BaseViewModel<CartState, CartEffects>(CartState()) {

    init {
        observeProgress {
            setState { copy(isLoading = it) }
        }

        getCart()
    }

    fun onBuyClick() {
        setEffect(CartEffects.NavigateToCheckout)
    }

    fun deleteFromCart() {
        // todo
    }

    fun updateQuantity() {
        // todo
    }

    fun onClearCartBtnClick() {
        setEffect(CartEffects.ShowClearCartConfirmationDialog)
    }

    fun onClearCartClick() {
        viewModelScope.launch {
            clearCartUseCase.call(Unit)
                .onError {
                    log { "kruci error ocurred" } // todo show error
                }
        }
    }

    fun retry() {
        getCart()
    }

    private fun getCart() {
        getCartProductsUseCase.call(Unit)
            .watchProgress(progress)
            .onSuccess {
                setState { copy(cartItems = it.toListItems(), error = null) }
            }
            .onError {
                setState { copy(error = it) }
            }
            .launchIn(viewModelScope)
    }

    private fun List<CartProduct>.toListItems() = map {
        CartItem.Product(
            id = it.product.id,
            quantityFormatted = it.quantity.toString(),
            priceFormatted = cartFormatter.formatPrice(it.product.price),
            thumbnailSrc = it.product.thumbnailSrc,
            name = it.product.name,
            price = it.product.price,
            cost = cartFormatter.formatPrice(it.product.price * it.quantity)
        )
    }.addItemIf(
        predicate = { isNotEmpty() },
        item = CartItem.Summary(cost = cartFormatter.formatPrice(getCartSum()))
    )

    private fun List<CartProduct>.getCartSum() =
        this.sumOf { it.quantity * it.product.price }

    data class CartState(
        val cartItems: List<CartItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: DefaultError? = null
    ) {
        val isEmptyPlaceholderVisible
            get() = cartItems.isEmpty() && !isLoading && error == null

        val isClearCartBtnVisible
            get() = !isEmptyPlaceholderVisible
    }

    sealed interface CartEffects : Effect {
        object NavigateToCheckout : CartEffects
        object ShowClearCartConfirmationDialog : CartEffects
    }
}

