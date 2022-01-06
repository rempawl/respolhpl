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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.R
import com.example.respolhpl.bindingAdapters.Converter
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.databinding.ProductDetailsFragmentBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.OnPageChangeCallbackImpl
import com.example.respolhpl.utils.autoCleared
import com.example.respolhpl.utils.event.EventObserver
import dagger.hilt.android.AndroidEntryPoint

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
            card.setOnClickListener { viewModel.navigate() }
        }
        setupObservers()
        binding?.setupBinding()

    }


    private fun navigateToFullScreenImageFragment(curPage: Int) {
        findNavController().navigate(
            ProductDetailsFragmentDirections.navigationProductDetailsToFullScreenImagesFragment(
                args.productId, curPage
            )
        )
        viewModel.doneNavigating()
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            res.checkIfIsSuccessAndType<Product>()?.let { prod ->
                imagesAdapter.submitList(prod.images)
            }
        }
        viewModel.shouldNavigate.observe(viewLifecycleOwner, EventObserver { curPage ->
            navigateToFullScreenImageFragment(curPage)
        }
        )
        viewModel.cartModel.cartQuantity.observe(viewLifecycleOwner) {
            if (binding?.quantity?.text.toString() != it.toString()) {
                binding?.quantity?.setText(it.toString())
            }
        }
        viewModel.cartModel.addToCartCount.observe(viewLifecycleOwner, EventObserver { count ->
            showAddToCartToast(count)
        })
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

        quantity.setText(viewModel1.cartModel.currentCartQuantity.toString())
        quantity.doOnTextChanged { text, _, _, _ ->

            viewModel1.cartModel.currentCartQuantity = Converter.stringToInt(text.toString())

        }
        toolbar.backBtn.setOnClickListener { findNavController().navigateUp() }
        toolbar.label.text = getString(R.string.product)

        viewModel = viewModel1
        errorRoot.retryButton.setOnClickListener { viewModel1.retry() }

        viewPager.adapter = imagesAdapter
        lifecycleOwner = viewLifecycleOwner
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        cartModel = viewModel1.cartModel
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



