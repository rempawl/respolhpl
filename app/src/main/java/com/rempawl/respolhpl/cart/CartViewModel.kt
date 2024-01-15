package com.rempawl.respolhpl.cart

import androidx.lifecycle.viewModelScope
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects
import com.rempawl.respolhpl.cart.CartViewModel.CartState
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.usecase.ClearCartUseCase
import com.rempawl.respolhpl.data.usecase.GetCartProductsUseCase
import com.rempawl.respolhpl.list.BaseListItem
import com.rempawl.respolhpl.list.UniqueListItem
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.Effect
import com.rempawl.respolhpl.utils.PriceFormatter
import com.rempawl.respolhpl.utils.extensions.addItemIf
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import com.rempawl.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val priceFormatter: PriceFormatter,
    private val clearCartUseCase: ClearCartUseCase
) : BaseViewModel<CartState, CartEffects>(CartState()) {

    init {
        observeProgress {
            setState { copy(isLoading = it) }
        }

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
            quantity = it.quantity.toString(),
            price = priceFormatter.format(it.product.price),
            thumbnailSrc = it.product.thumbnailSrc,
            name = it.product.name
        )
    }.addItemIf(
        predicate = { isNotEmpty() },
        item = CartItem.Summary(cost = priceFormatter.format(getCartSum()))
    )

    private fun List<CartProduct>.getCartSum() =
        this.sumOf { it.quantity * it.product.price }

    fun onBuyClick() {
        setEffect(CartEffects.NavigateToCheckout)
    }

    fun deleteFromCart() {
        // todo
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase.call(Unit)
        }
        // todo test etcc..
    }

    fun retry() {
        getCart()
    }

    data class CartState(
        val cartItems: List<CartItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: DefaultError? = null
    ) {
        val isEmptyPlaceholderVisible
            get() = cartItems.isEmpty() && !isLoading && error == null
    }

    sealed class CartItem : BaseListItem {
        data class Product(
            val quantity: String,
            val id: Int,
            val price: String,
            val thumbnailSrc: String?,
            val name: String,
            override val itemId: Any = id
        ) : CartItem(), BaseListItem

        data class Summary(
            val cost: String,
        ) : CartItem(), BaseListItem by UniqueListItem()
    }

    sealed interface CartEffects : Effect {
        object NavigateToCheckout : CartEffects
    }
}

