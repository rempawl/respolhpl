package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.databinding.ProductDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {


    private val arg: ProductDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var imagesAdapter: ImagesAdapter

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProductDetailsFragmentBinding.inflate(inflater)
        setupObservers()
        setupBinding(binding)
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

    private fun setupBinding(binding: ProductDetailsFragmentBinding) {
        binding.viewModel = viewModel
        binding.viewPager.adapter = imagesAdapter
        binding.lifecycleOwner = viewLifecycleOwner
    }


}