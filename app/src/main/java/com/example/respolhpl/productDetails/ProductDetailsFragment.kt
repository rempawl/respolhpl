package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

        setupObservers()
        setupBinding()
        return binding.root
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
        })
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.viewPager.adapter = imagesAdapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }


    companion object {
        const val prodId = "productId"
        const val CURRENT_VIEW_PAGER_ITEM = "currentItem"
    }


}