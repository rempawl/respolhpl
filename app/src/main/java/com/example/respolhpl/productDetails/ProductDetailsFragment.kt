package com.example.respolhpl.productDetails

import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.R
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.databinding.FragmentProductDetailsBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.autoCleared
import com.example.respolhpl.utils.extensions.setTextIfDifferent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges

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
        imagesAdapter = ImagesAdapter {
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            card.clicks()
                .flatMapLatest { viewModel.navigateToFullScreenImage() }
                .onEach { images -> navigateToFullScreenImageFragment(images) }
                .launchIn(lifecycleScope)
        }
        binding?.setupBinding()
        setupObservers()
    }


    private fun navigateToFullScreenImageFragment(images: Images) {
        val currentItem = binding!!.viewPager.currentItem
        Log.d("kruci", "navigateToFullScreenImageFragment: $currentItem")

        findNavController().navigate(
            ProductDetailsFragmentDirections.navigationProductDetailsToFullScreenImagesFragment(
                /* productId = */ args.productId,
                /* currentPage = */currentItem,
                /* images = */images
            )
        )
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        with(binding!!) {
                            productDetails.isVisible = state.isSuccess
                            loading.root.isVisible = state.isLoading
                            errorRoot.root.isVisible = state.error != null
                        }
                    }
                }
                launch {
                    viewModel.product
                        .collectLatest { product ->
                            imagesAdapter.submitList(product.images)
                            setProduct(product)
                        }
                }

                launch {
                    viewModel.cartQuantity.collectLatest { quantity ->
                        binding?.quantity?.setTextIfDifferent(quantity.toString())
                    }
                }

                viewModel.isPlusBtnEnabled
                    .onEach { binding!!.plusBtn.isEnabled = it }
                    .launchIn(this)

                viewModel.isMinusBtnEnabled
                    .onEach { binding!!.minusBtn.isEnabled = it }
                    .launchIn(this)
            }
        }
    }

    private fun setProduct(product: Product) {
        with(binding!!) {
            toolbar.label.text = product.name
            prodPrice.text = resources.getString(R.string.price, product.price)
            prodDesc.text = SpannableString(
                HtmlCompat.fromHtml(
                    product.description,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            prodName.text = product.name
            maxQuantity.text = resources.getString(R.string.product_max_quantity, product.quantity)
        }
    }

    private fun showAddToCartToast(count: Int) {
        Toast.makeText(
            requireContext(),
            resources.getQuantityString(R.plurals.add_to_cart_quantity, count, count),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun FragmentProductDetailsBinding.setupBinding() {
        with(toolbar) {
            cartBtn.setOnClickListener {
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsToCartFragment()) //todo some extension on base fragment
            }
            backBtn.setOnClickListener { findNavController().navigateUp() }
            label.text = getString(R.string.product)
        }
        plusBtn.setOnClickListener { viewModel.onPlusBtnClick() }
        minusBtn.setOnClickListener { viewModel.onMinusBtnClick() }
        viewModel.setQuantityChangedListener(quantity.textChanges())

        errorRoot.retryButton.setOnClickListener { viewModel.retry() }
        with(viewPager) {
            adapter = imagesAdapter
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }

    companion object {
        const val CURRENT_VIEW_PAGER_ITEM = "currentItem"
    }
}




