package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.respolhpl.databinding.FragmentFullScreenImagesBinding
import com.example.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.example.respolhpl.utils.autoCleared
import com.example.respolhpl.utils.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullScreenImagesFragment : Fragment() {

    private val viewModel by viewModels<ProductImagesViewModel>()
    private var imagesAdapter by autoCleared<ImagesAdapter>()
    private var binding: FragmentFullScreenImagesBinding? = null
    private val navArgs by navArgs<FullScreenImagesFragmentArgs>()

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
        observeOnStart {
            launch {
                viewModel.state.collectLatest { state ->
                    imagesAdapter.submitList(state.images.images)
                    with(binding!!) {
                        errorRoot.root.isVisible = state.error != null
                    }
                }
            }
            launch {
                viewModel.showError.collectLatest { error ->
                    showToast(error.getErrorMessage())
                }
            }
        }
    }


    private fun FragmentFullScreenImagesBinding.setupBinding() {
        binding!!.viewPager.currentItem = navArgs.currentPage

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
