package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.R
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

    private var binding: ProductDetailsFragmentBinding by autoCleared()

    private val viewModel: ProductDetailsViewModel by viewModels()

    private val onPageChangeCallback by lazy { OnPageChangeCallbackImpl(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailsFragmentBinding.inflate(inflater, container, false)
        imagesAdapter = ImagesAdapter {
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            card.setOnClickListener { viewModel.navigate() }
        }
        setHasOptionsMenu(true)
        setupObservers()
        setupBinding()
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_fav).isVisible = true
    }

    private fun navigateToFullScreenImageDialog(curPage: Int) {
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
            navigateToFullScreenImageDialog(curPage)
        }
        )
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

    private fun setupBinding() {
        val vm = viewModel
        binding.apply {
            errorRoot.retryButton.setOnClickListener { vm.retry() }
            viewModel = vm
            viewPager.adapter = imagesAdapter
            lifecycleOwner = viewLifecycleOwner
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
            cartModel = vm.cartModel

        }
    }


    companion object {
        const val prodId = "productId"
        const val CURRENT_VIEW_PAGER_ITEM = "currentItem"
    }


}



