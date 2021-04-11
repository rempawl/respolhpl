package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.databinding.FullScreenImagesFragmentBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.OnPageChangeCallbackImpl
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenImagesFragment : Fragment() {

    val viewModel by viewModels<FullScreenImagesViewModel>()
    var imagesAdapter by autoCleared<ImagesAdapter>()
    var binding by autoCleared<FullScreenImagesFragmentBinding>()

    private val onPageChangeCallback by lazy { OnPageChangeCallbackImpl(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FullScreenImagesFragmentBinding.inflate(inflater, container, false)
        imagesAdapter = ImagesAdapter { }

        setupObservers()
        setupBinding()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            res.checkIfIsSuccessAndListOf<Image>()?.let { imgs ->
                imagesAdapter.submitList(imgs)
            }
        }
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.viewPager.adapter = imagesAdapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

//    override fun onDestroyView() {
//        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
//        super.onDestroyView()
//    }

}