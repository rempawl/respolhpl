package com.example.respolhpl.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respolhpl.R
import com.example.respolhpl.cart.CartViewModel.CartEffects.NavigateToCheckout
import com.example.respolhpl.databinding.FragmentCartBinding
import com.example.respolhpl.productDetails.observeOnStart
import com.example.respolhpl.productDetails.showToast
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.autoCleared
import com.example.respolhpl.utils.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// todo compose??
@AndroidEntryPoint
class CartFragment : Fragment() {

    @Inject
    lateinit var dispatchersProvider: DispatchersProvider

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
        binding.setupBinding()
        setupObservers()
    }

//    private fun onDeleteBtnClick(prod: CartItem.CartProduct) {
//        ConfirmDialog.newInstance(getString(R.string.deletion_confirmation_title)) {
//            viewModel.deleteFromCart(prod)
//        }.show(childFragmentManager, "")
//    }


    private fun setupObservers() {
        observeOnStart {
            launch {
                viewModel.effects.collect {
                    when (it) {
                        NavigateToCheckout -> showToast("Funkcjonalność w przygotowaniu")
                    }
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
                    .collectLatest {error ->
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
            clearBtn.isVisible = !isEmptyPlaceholderVisible
        }
    }


    private fun FragmentCartBinding.setupBinding() {
        setupProductsListAdapter()
        backBtn.setOnClickListener { findNavController().navigateUp() }
        clearBtn.setOnClickListener {
            ConfirmDialog.newInstance(getString(R.string.confirm_clear_cart)) {
                this@CartFragment.viewModel.clearCart()
            }.show(childFragmentManager, "")
        }
        label.setText(R.string.cart)
    }

    private fun FragmentCartBinding.setupProductsListAdapter() {
        prodsList.apply {
            adapter = this@CartFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }
    }
}
