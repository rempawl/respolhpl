package com.rempawl.respolhpl.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnLifecycleDestroyed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.rempawl.respolhpl.R
import com.rempawl.respolhpl.checkout.CheckoutAction.ProductsClick
import com.rempawl.respolhpl.databinding.FragmentCheckoutBinding
import com.rempawl.respolhpl.list.listItems
import com.rempawl.respolhpl.productDetails.observeOnStart
import com.rempawl.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var binding: FragmentCheckoutBinding by autoCleared()
    private val viewModel: CheckoutViewModel by viewModels()

    // todo get shipment
    // todo get payment
    // todo create order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater)
        binding.composeView.apply {
            setViewCompositionStrategy(DisposeOnLifecycleDestroyed(viewLifecycleOwner))

            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                MaterialTheme {
                    CheckoutScreen(state, submitAction = { viewModel.submitAction(it) })
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnStart {
            viewModel.effects
                .onEach { effect ->
                    when(effect) {
                        CheckoutEffect.BuyClick -> TODO()
                        CheckoutEffect.DiscountClick -> TODO()
                        CheckoutEffect.InvoiceClick -> TODO()
                        CheckoutEffect.NavigateBack -> findNavController().navigateUp()
                        CheckoutEffect.OpenProductsBottomSheet -> TODO()
                        CheckoutEffect.PaymentClick -> TODO()
                        CheckoutEffect.PersonalDataClick -> TODO()
                        CheckoutEffect.RulesClick -> TODO()
                        CheckoutEffect.ShipmentClick -> TODO()
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    companion object {
        fun newInstance() = CheckoutFragment()
    }
}


@Composable
private fun CheckoutScreen(
    state: CheckoutState,
    submitAction: (CheckoutAction) -> Unit
) {
    val lightRed = Color(0xFFAA3333)
    Scaffold(
        topBar = {
            // todo theme colors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(lightRed)
            ) {
                IconButton(onClick = { submitAction(CheckoutAction.BackClick) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back button",
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                ProductsSection(state, submitAction)
                ShipmentSection()
                PersonalDataSection()
                PaymentSection()
                SummarySection()
                DiscountSection()
                RulesSection()
                SubmitSection()
            }
        }
    }
}


@Composable
private fun ProductsSection(state: CheckoutState, onClick: (CheckoutAction) -> Unit) {
    Surface(
        color = Color.White,
        modifier = Modifier.clickable { onClick(ProductsClick) }
    ) {
        Text(
            text = " ${state.productsCount} products",
            style = MaterialTheme.typography.headlineMedium
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listItems(state.productItems) { product ->
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(56.dp),
                        painter = rememberAsyncImagePainter(model = product.imageUrl),
                        contentDescription = "product image"
                    )
                }
            }
        }
    }
    // horizontal list of products
    // product images, on click opens bottomSheet with products list
}

@Composable
private fun ShipmentSection() {
    // shipment options, on click opens bottomSheet with shipment options
    // adresses, on click opens fragment with adresses
}

@Composable
private fun PersonalDataSection() {
    // invoice picker (faktura, paragon), on click opens fragment with invoice options
}

@Composable
private fun PaymentSection() {
    // payment options, on click opens bottomSheet with payment options
}

@Composable
private fun SummarySection() {
    // summary of order
}

@Composable
private fun DiscountSection() {
    // discount code
}

@Composable
private fun RulesSection() {
    // rules checkbox, webview with rules
}

@Composable
private fun SubmitSection() {
    // submit button
}

@Preview
@Composable
private fun ProductsPreview() {
//    ProductsSection()
}