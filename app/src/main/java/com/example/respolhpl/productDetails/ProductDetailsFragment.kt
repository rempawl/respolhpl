package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.databinding.ProductDetailsFragmentBinding
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {


    private val arg: ProductDetailsFragmentArgs by navArgs()

    private var imagesAdapter: ImagesAdapter by autoCleared()
    private var binding: ProductDetailsFragmentBinding by autoCleared()
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailsFragmentBinding.inflate(inflater)
        imagesAdapter = ImagesAdapter()

        setupObservers()
        setupBinding()
        return binding.root
    }


    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { prod ->
            prod.takeIf { it.isSuccess }?.let { res ->
                check(res is Result.Success<*> && res.data is Product)
                imagesAdapter.submitList(res.data.images)
            }
        }
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.viewPager.adapter = imagesAdapter
        binding.lifecycleOwner = viewLifecycleOwner
    }


}