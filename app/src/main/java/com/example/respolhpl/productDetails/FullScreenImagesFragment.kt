package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.databinding.FragmentFullScreenImagesBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullScreenImagesFragment : Fragment() {
    //todo pass single image

    private val viewModel by viewModels<ProductImagesViewModel>()
    var imagesAdapter by autoCleared<ImagesAdapter>()
    var binding: FragmentFullScreenImagesBinding? = null
    val navArgs by navArgs<FullScreenImagesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullScreenImagesBinding.inflate(inflater, container, false)
        imagesAdapter = ImagesAdapter()

        binding!!.setupBinding()
        setupObservers()
        return binding!!.root
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        imagesAdapter.submitList(state.images.images)
                        with(binding!!) {
                            errorRoot.root.isVisible = state.error != null
                        }
                    }
                }
            }
        }
        binding!!.viewPager.currentItem = navArgs.currentPage
    }

    private fun FragmentFullScreenImagesBinding.setupBinding() {
        with(viewPager) {
            adapter = imagesAdapter
        }
        backBtn.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
