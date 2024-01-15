package com.rempawl.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.rempawl.respolhpl.R
import com.rempawl.respolhpl.data.model.domain.Images
import com.rempawl.respolhpl.databinding.FragmentProductDetailsBinding
import com.rempawl.respolhpl.productDetails.ProductDetailsFragmentDirections.actionProductDetailsToCartFragment
import com.rempawl.respolhpl.productDetails.ProductDetailsFragmentDirections.navigationProductDetailsToFullScreenImagesFragment
import com.rempawl.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.rempawl.respolhpl.utils.autoCleared
import com.rempawl.respolhpl.utils.compose.BottomSheetPicker
import com.rempawl.respolhpl.utils.compose.HeightSpacer
import com.rempawl.respolhpl.utils.extensions.QuantityCounter
import com.rempawl.respolhpl.utils.extensions.dpToPx
import com.rempawl.respolhpl.utils.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args: ProductDetailsFragmentArgs by navArgs()

    private var imagesAdapter: ImagesAdapter by autoCleared()

    private var binding: FragmentProductDetailsBinding? = null
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                if (state.showVariantPicker) {
                    BottomSheetPicker(
                        items = state.variants,
                        title = stringResource(R.string.select_product_variant_title),
                        onDismiss = { viewModel.onCloseVariantPicker() },
                        onItemClick = { viewModel.onVariantClicked(it) },
                        itemView = {
                            Text(
                                text = it.attributesFormatted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
                PriceQuantityAndVariantSection(state)
            }

            imagesAdapter = ImagesAdapter(
                bindingDecorator = {
                    image.scaleType = ImageView.ScaleType.CENTER_CROP
                    card.setOnClickListener { viewModel.onImageClicked() }

                    card.updateLayoutParams<RecyclerView.LayoutParams> {
                        updateMargins(
                            left = 16.dpToPx(resources),
                            top = 16.dpToPx(resources),
                            right = 16.dpToPx(resources),
                            bottom = 16.dpToPx(resources)
                        )
                    }
                }
            )
            binding!!.setupBinding()
            setupObservers()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


    @Composable
    private fun PriceQuantityAndVariantSection(state: ProductDetailsState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .imePadding()
        ) {
            Text(
                text = state.productName,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = state.priceFormatted,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            )
            ProductVariantSection(state)
            HeightSpacer()
            QuantitySection(state)
            HeightSpacer()
        }
    }

    @Composable
    private fun QuantitySection(state: ProductDetailsState) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuantityCounter(
                onMinusClick = { viewModel.onMinusBtnClick() },
                onPlusClick = { viewModel.onPlusBtnClick() },
                quantityText = state.cartQuantity.toString(),
                onQuantityChange = { viewModel.onQuantityChanged(it) },
                isPlusBtnEnabled = state.isPlusBtnEnabled,
                isMinusBtnEnabled = state.isMinusBtnEnabled
            )
            Text(
                text = stringResource(
                    R.string.product_max_quantity,
                    state.maxQuantity
                )
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun ProductVariantSection(state: ProductDetailsState) {
        if (state.isVariantCardVisible) {
            HeightSpacer()
            OutlinedCard(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onClick = { viewModel.onPickVariationBtnClick() }
            ) {

                Text(
                    text = stringResource(R.string.current_variant_label),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text = state.currentVariant!!.attributesFormatted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

            }
        }
    }


    private fun navigateToFullScreenImageFragment(images: Images) {
        val currentItem = binding!!.imagesViewPager.currentItem

        findNavController().navigate(
            navigationProductDetailsToFullScreenImagesFragment(
                /* productId = */ args.productId,
                /* currentPage = */currentItem,
                /* images = */images
            )
        )
    }

    private fun setupObservers() {
        observeOnStart {
            viewModel.state
                .onEach { state -> setupView(state) }
                .launchIn(lifecycleScope)

            launch {
                viewModel.showError.collectLatest {
                    showToast(it.getErrorMessage())
                }
            }
            launch {
                viewModel.effects.collectLatest { effect ->
                    when (effect) {
                        is ProductDetailsEffect.ItemAddedToCart -> showAddToCartToast(effect.quantity)
                        is ProductDetailsEffect.NavigateToFullScreenImage -> navigateToFullScreenImageFragment(
                            effect.images
                        )
                    }
                }
            }
        }
    }

    private fun setupView(state: ProductDetailsState) = with(binding!!) {
        toolbar.label.text = state.toolbarLabel
        prodDesc.text = state.descriptionFormatted
        productDetails.isVisible = state.isSuccess
        loading.root.isVisible = state.showProgress
        errorRoot.root.isVisible = state.productError != null
        imagesAdapter.submitList(state.images.images)
    }


    private fun showAddToCartToast(count: Int) {
        showToast(
            resources.getQuantityString(R.plurals.add_to_cart_quantity, count, count),
            Toast.LENGTH_LONG
        )
    }

    private fun FragmentProductDetailsBinding.setupBinding() {
        with(toolbar) {
            cartBtn.setOnClickListener {
                findNavController().navigate(actionProductDetailsToCartFragment()) //todo some extension on base fragment
            }
            backBtn.setOnClickListener { findNavController().navigateUp() }
            label.text = getString(R.string.product)
        }
        addToCartButton.setOnClickListener { viewModel.onAddToCartClick() }
        errorRoot.retryButton.setOnClickListener { viewModel.retry() }
        buyNowBtn.setOnClickListener {
//            findNavController().navigate(ProductDetailsFragmentDirections.actionDetailsFragmentToCheckoutFragment())

            showToast(getString(R.string.work_in_progress))
        }
        with(imagesViewPager) {
            adapter = imagesAdapter
            dotsIndicator.attachTo(this)
        }
    }

    companion object {
        const val CURRENT_VIEW_PAGER_ITEM = "currentItem"
    }
}




