package com.rempawl.respolhpl.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rempawl.respolhpl.R
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects.NavigateToCheckout
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects.ShowClearCartConfirmationDialog
import com.rempawl.respolhpl.databinding.FragmentCartBinding
import com.rempawl.respolhpl.productDetails.observeOnStart
import com.rempawl.respolhpl.productDetails.showToast
import com.rempawl.respolhpl.utils.autoCleared
import com.rempawl.respolhpl.utils.extensions.addStatusBarPaddingForAndroid15
import com.rempawl.respolhpl.utils.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val viewModel: CartViewModel by viewModels()
    private var adapter by autoCleared<CartProductAdapter>()
    private var binding by autoCleared<FragmentCartBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartProductAdapter(viewModel::onBuyClick)
        binding.toolbar.root.setOnApplyWindowInsetsListener { v, windowInsets ->
            v.addStatusBarPaddingForAndroid15(windowInsets)
        }
        binding.setupBinding()
        setupObservers()
    }


    private fun setupObservers() {
        observeOnStart {
            launch {
                viewModel.effects.collect {
                    when (it) {
                        NavigateToCheckout -> showToast(
                            getString(R.string.work_in_progress),
                            Toast.LENGTH_SHORT
                        )

                        ShowClearCartConfirmationDialog -> showClearCartConfirmationDialog()
                    }
                }
            }
            launch {
                viewModel.mapStateDistinct { it.isClearCartBtnVisible }
                    .collectLatest {
                        binding.toolbar.actionBtn.isVisible = it
                    }
            }
            launch {
                viewModel.mapStateDistinct { it.cartItems }
                    .collectLatest {
                        adapter.submitList(it)
                    }
            }
            launch {
                viewModel.mapStateDistinct { it.error }
                    .collectLatest { error ->
                        with(binding.errorView) {
                            rootView.isVisible = error != null
                            errorHeader.text = error?.getErrorMessage()
                            retryButton.setOnClickListener { viewModel.retry() }
                        }
                    }
            }
            launch {
                viewModel
                    .mapStateDistinct { it.isEmptyPlaceholderVisible }
                    .collectLatest {
                        updateEmptyView(it)
                    }
            }
            launch {
                viewModel.mapStateDistinct { it.isLoading }
                    .collectLatest {
                        binding.loading.root.isVisible = it
                    }
            }
        }
    }


    private fun updateEmptyView(isEmptyPlaceholderVisible: Boolean) {
        binding.apply {
            emptyCartText.isVisible = isEmptyPlaceholderVisible
            emptyCartIcon.isVisible = isEmptyPlaceholderVisible
        }
    }


    private fun FragmentCartBinding.setupBinding() {
        setupProductsListAdapter()
        with(toolbar) {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            actionBtn.setImageResource(R.drawable.ic_clear)
            actionBtn.setOnClickListener {
                viewModel.onClearCartBtnClick()
            }
            label.setText(R.string.cart)
        }
    }

    private fun showClearCartConfirmationDialog() {
        ConfirmDialog.newInstance(getString(R.string.confirm_clear_cart)) {
            this@CartFragment.viewModel.onClearCartClick()
        }.show(childFragmentManager, "")
    }

    private fun FragmentCartBinding.setupProductsListAdapter() {
        prodsList.apply {
            adapter = this@CartFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }
    }
}
