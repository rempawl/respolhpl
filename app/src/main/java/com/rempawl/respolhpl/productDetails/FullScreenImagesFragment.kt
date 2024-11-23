package com.rempawl.respolhpl.productDetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rempawl.respolhpl.databinding.FragmentFullScreenImagesBinding
import com.rempawl.respolhpl.productDetails.imagesAdapter.ImagesAdapter
import com.rempawl.respolhpl.utils.autoCleared
import com.rempawl.respolhpl.utils.getErrorMessage
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
        requireActivity().window.enableFullScreenMode()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.setupBinding()
        setupObservers()
    }

    override fun onDestroyView() {
        binding = null
        requireActivity().window.disableFullScreenMode()
        super.onDestroyView()
    }

    private fun setupObservers() {
        observeOnStart {
            launch {
                viewModel.state.collectLatest { state ->
                    imagesAdapter.submitList(state.images.images)
                    with(binding!!) {
                        imagesViewPager.currentItem = navArgs.currentPage // todo move to viewModel
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

    private fun Window.enableFullScreenMode() {
        WindowCompat.setDecorFitsSystemWindows(this, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            WindowCompat.getInsetsController(this, decorView).run {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun FragmentFullScreenImagesBinding.setupBinding() {
        with(imagesViewPager) {
            adapter = imagesAdapter
            dotsIndicator.attachTo(this)
        }
        backBtn.setOnClickListener { findNavController().navigateUp() }
    }

    private fun Window.disableFullScreenMode() {
        WindowCompat.setDecorFitsSystemWindows(this, true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            WindowCompat.getInsetsController(this, decorView)
                .show(WindowInsetsCompat.Type.systemBars())
        }
    }
}
