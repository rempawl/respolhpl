package com.example.respolhpl.cart

import androidx.lifecycle.viewModelScope
import com.example.respolhpl.cart.CartViewModel.CartEffects
import com.example.respolhpl.cart.CartViewModel.CartState
import com.example.respolhpl.data.model.domain.CartProduct
import com.example.respolhpl.data.usecase.GetCartProductsUseCase
import com.example.respolhpl.paging.BaseListItem
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.DefaultError
import com.example.respolhpl.utils.Effect
import com.example.respolhpl.utils.PriceFormatter
import com.example.respolhpl.utils.extensions.addItemIf
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import com.example.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val priceFormatter: PriceFormatter
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
        // todo
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
        ) : CartItem() {
            override val itemId: Any = javaClass.simpleName
        }
    }

    sealed interface CartEffects : Effect {
        object NavigateToCheckout : CartEffects
    }
}

open class UniqueListItem : BaseListItem {
    override val itemId: Any = this::class.java.simpleName
}

