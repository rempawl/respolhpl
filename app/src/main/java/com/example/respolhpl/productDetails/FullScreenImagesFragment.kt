package com.example.respolhpl.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
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
    var binding : FullScreenImagesFragmentBinding? = null

    private val onPageChangeCallback by lazy { OnPageChangeCallbackImpl(viewModel) }

    private val args by navArgs<FullScreenImagesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FullScreenImagesFragmentBinding.inflate(inflater, container, false)
        imagesAdapter = ImagesAdapter {}

        setHasOptionsMenu(true)
        setupObservers()
        setupBinding()
        return binding!!.root
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            res.checkIfIsSuccessAndListOf<Image>()?.let { imgs ->
                imagesAdapter.submitList(imgs)
            }
        }
    }

    private fun setupBinding() {
        val vm = viewModel
        binding?.apply {
            viewModel = vm

            errorRoot.retryButton.setOnClickListener { vm.retry() }

            viewPager.adapter = imagesAdapter
            lifecycleOwner = viewLifecycleOwner
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    override fun onDestroyView() {
        binding?.viewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding = null
        super.onDestroyView()
    }

}