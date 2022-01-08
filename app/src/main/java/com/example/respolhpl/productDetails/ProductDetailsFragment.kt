package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.R
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.databinding.ProductDetailsFragmentBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.OnPageChangeCallbackImpl
import com.example.respolhpl.utils.autoCleared
import com.example.respolhpl.utils.extensions.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args: ProductDetailsFragmentArgs by navArgs()

    private var imagesAdapter: ImagesAdapter by autoCleared()

    private var binding: ProductDetailsFragmentBinding? = null

    private val viewModel: ProductDetailsViewModel by viewModels()

    private val onPageChangeCallback by lazy { OnPageChangeCallbackImpl(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailsFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagesAdapter = ImagesAdapter {
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            card.clicks()
                .flatMapLatest { viewModel.navigateToFullScreenImage() }
                .onEach { navigateToFullScreenImageFragment(it) }
                .launchIn(lifecycleScope)
        }
        binding?.setupBinding()
        setupObservers()

    }


    private fun navigateToFullScreenImageFragment(curPage: Int) {
        findNavController().navigate(
            ProductDetailsFragmentDirections.navigationProductDetailsToFullScreenImagesFragment(
                args.productId, curPage
            )
        )
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.result.collectLatest {
                        it.checkIfIsSuccessAndType<Product>()?.let { prod ->
                            imagesAdapter.submitList(prod.images)
                        }
                    }
                }
                launch {
                    viewModel.cartQuantity.collectLatest {
                        if (binding?.quantity?.text.toString() != it.toString()) {
                            binding?.quantity?.setText(it.toString())
                        }
                    }
                }
                viewModel.isPlusBtnEnabled
                    .onEach {
                        binding?.plusBtn?.isEnabled = it
                    }
                    .launchIn(this)
                viewModel.isMinusBtnEnabled
                    .onEach {
                        binding?.minusBtn?.isEnabled = it
                    }
                    .launchIn(this)


            }
//                viewModel.shouldNavigate.observe(viewLifecycleOwner, EventObserver { curPage ->
//                    navigateToFullScreenImageFragment(curPage)
//                }
//                )

        }
    }


    private fun showAddToCartToast(count: Int) {
        Toast.makeText(
            requireContext(),
            resources.getQuantityString(R.plurals.add_to_cart_quantity, count, count),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun ProductDetailsFragmentBinding.setupBinding() {
        val viewModel1 = this@ProductDetailsFragment.viewModel
        toolbar.cartBtn.setOnClickListener {
            findNavController().navigate(
                ProductDetailsFragmentDirections.actionProductDetailsToCartFragment()
            )
        }


//        quantity.setText(viewModel1.currentCartQuantity.toString())
        quantity.doOnTextChanged { text, _, _, _ ->

//            viewModel1.cartModel.currentCartQuantity = Converter.stringToInt(text.toString())

        }
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addToCartButton.clicks().flatMapLatest {
                    viewModel1.onAddToCartClick()
                }.collect {
                    showAddToCartToast(it)
                }
            }
        }
        toolbar.backBtn.setOnClickListener { findNavController().navigateUp() }
        toolbar.label.text = getString(R.string.product)

        viewModel = viewModel1
        errorRoot.retryButton.setOnClickListener { viewModel1.retry() }

        viewPager.adapter = imagesAdapter
        lifecycleOwner = viewLifecycleOwner
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDestroyView() {
        binding?.viewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding = null
        super.onDestroyView()

    }

    companion object {
        const val prodId = "productId"
        const val CURRENT_VIEW_PAGER_ITEM = "currentItem"
    }
}



