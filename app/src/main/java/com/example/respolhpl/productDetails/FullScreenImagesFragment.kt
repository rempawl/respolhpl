package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.databinding.FullScreenImagesFragmentBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.OnPageChangeCallbackImpl
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullScreenImagesFragment : Fragment() {

    private val viewModel by viewModels<ProductImagesViewModel>()
    var imagesAdapter by autoCleared<ImagesAdapter>()
    var binding: FullScreenImagesFragmentBinding? = null

    private val onPageChangeCallback by lazy { OnPageChangeCallbackImpl(viewModel) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FullScreenImagesFragmentBinding.inflate(inflater, container, false)
        imagesAdapter = ImagesAdapter {}

        setupObservers()
        setupBinding()
        return binding!!.root
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collectLatest { res ->
                    res.checkIfIsSuccessAndListOf<Image>()?.let { imgs ->
                        imagesAdapter.submitList(imgs)
                    }
                }
            }
        }
    }

    private fun setupBinding() {
        binding?.apply {
            errorRoot.retryButton.setOnClickListener { viewModel.retry() }

            viewPager.adapter = imagesAdapter
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    override fun onDestroyView() {
        binding?.viewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding = null
        super.onDestroyView()
    }

}
