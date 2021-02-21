package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.databinding.ProductDetailsFragmentBinding
import com.example.respolhpl.productDetails.ProductDetailsViewModel.ProductDetailsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {


    private val arg: ProductDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var productDetailsViewModelFactory: ProductDetailsViewModelFactory
    private val viewModel: ProductDetailsViewModel by viewModels {
        ProductDetailsViewModel.provideFactory(productDetailsViewModelFactory, arg.productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProductDetailsFragmentBinding.inflate(inflater)
        setupBinding(binding)
        return binding.root

    }

    private fun setupBinding(binding: ProductDetailsFragmentBinding) {
        binding.viewModel = viewModel
    }


}