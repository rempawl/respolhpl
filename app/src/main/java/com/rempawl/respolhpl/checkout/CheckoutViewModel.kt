package com.rempawl.respolhpl.checkout

import androidx.lifecycle.viewModelScope
import com.rempawl.respolhpl.checkout.CheckoutAction.BackClick
import com.rempawl.respolhpl.checkout.CheckoutAction.BuyClick
import com.rempawl.respolhpl.checkout.CheckoutAction.DiscountClick
import com.rempawl.respolhpl.checkout.CheckoutAction.InvoiceClick
import com.rempawl.respolhpl.checkout.CheckoutAction.PaymentClick
import com.rempawl.respolhpl.checkout.CheckoutAction.PersonalDataClick
import com.rempawl.respolhpl.checkout.CheckoutAction.ProductsClick
import com.rempawl.respolhpl.checkout.CheckoutAction.RulesClick
import com.rempawl.respolhpl.checkout.CheckoutAction.ShipmentClick
import com.rempawl.respolhpl.data.usecase.GetCartProductsUseCase
import com.rempawl.respolhpl.list.BaseListItem
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.Effect
import com.rempawl.respolhpl.utils.PriceFormatter
import com.rempawl.respolhpl.utils.ProgressSemaphoreImpl
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
//    private val getShipmentUseCase: GetShipmentUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val priceFormatter: PriceFormatter,
    progressSemaphore: ProgressSemaphoreImpl
) : BaseViewModel<CheckoutState, CheckoutEffect>(CheckoutState(), progressSemaphore) {

    // todo nav args
    // differ checkout and cart
    // todo shipment
    // todo invoice
    // todo payment
    init {
        getCart()

    }

    fun onBuyClick() {
        //todo
    }

    fun getShipment() {

    }

    fun getCart() {
        // todo get checkout use case
        getCartProductsUseCase.call(Unit).onSuccess { cart ->
            setState {
                copy(
                    productItems = cart.map {
                        CheckoutProductItem(
                            id = it.product.id,
                            count = it.quantity,
                            price = priceFormatter.format(it.product.price),
                            name = it.product.name,
                            imageUrl = it.product.thumbnailSrc.orEmpty(),
                            priceSum = priceFormatter.format(it.product.price * it.quantity)
                        )
                    }
                )
            }
        }
            .onError {
                //todo
            }
            .launchIn(viewModelScope)

    }

    fun pickShipment() {

    }

    private fun onProductsClick() {
        setEffect(CheckoutEffect.OpenProductsBottomSheet)
    }

    fun submitAction(action: CheckoutAction) {
        when (action) {
            BuyClick -> onBuyClick()
            ShipmentClick -> getShipment()
            InvoiceClick -> getShipment()
            PaymentClick -> getShipment()
            RulesClick -> getShipment()
            DiscountClick -> getShipment()
            PersonalDataClick -> getShipment()
            ProductsClick -> onProductsClick()
            BackClick -> setEffect(CheckoutEffect.NavigateBack)
        }
    }
}

data class CheckoutProductItem(
    val id: Int,
    val count: Int,
    val price: String,
    val name: String,
    val imageUrl: String,
    val priceSum: String,
    override val itemId: Int = id
) : BaseListItem

sealed class CheckoutEffect : Effect {
    object BuyClick : CheckoutEffect()
    object ShipmentClick : CheckoutEffect()
    object InvoiceClick : CheckoutEffect()
    object PaymentClick : CheckoutEffect()
    object RulesClick : CheckoutEffect()
    object DiscountClick : CheckoutEffect()
    object PersonalDataClick : CheckoutEffect()
    object OpenProductsBottomSheet : CheckoutEffect()
    object NavigateBack : CheckoutEffect()
}

data class CheckoutState(
    val productItems: List<CheckoutProductItem> = emptyList(),
    val error: AppError? = null,
//    val shipment: Shipment,
//    val invoice: Invoice,
//    val payment: Payment,
//    val cart: CartItem,
//    val rules: Rules,
//    val discount: Discount,
//    val personalData: PersonalData,
//    val summary: CartItem.Summary,
//    val isBuyButtonEnabled: Boolean
) {
    val productsCount
        get() = productItems.size
}

sealed interface CheckoutAction {
    object BuyClick : CheckoutAction
    object ShipmentClick : CheckoutAction
    object InvoiceClick : CheckoutAction
    object PaymentClick : CheckoutAction
    object RulesClick : CheckoutAction
    object DiscountClick : CheckoutAction
    object PersonalDataClick : CheckoutAction
    object ProductsClick : CheckoutAction
    object BackClick : CheckoutAction
}