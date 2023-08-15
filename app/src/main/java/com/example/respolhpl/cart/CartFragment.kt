package com.example.respolhpl.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respolhpl.R
import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.databinding.FragmentCartBinding
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
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
        adapter = CartProductAdapter(dispatchersProvider) { prod -> onDeleteBtnClick(prod) }
        binding.setupBinding()
        setupObservers()
    }

    private fun onDeleteBtnClick(prod: CartItem.CartProduct) {
        ConfirmDialog.newInstance(getString(R.string.deletion_confirmation_title)) {
            viewModel.deleteFromCart(prod)
        }.show(childFragmentManager, "")
    }


    private fun setupObservers() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }


    private fun updateEmptyView(isEmpty: Boolean) {
        binding.apply {
            val vis = if (isEmpty) View.VISIBLE else View.GONE
            emptyCartText.visibility = vis
            emptyCartIcon.visibility = vis
            clearBtn.visibility = if (isEmpty) View.GONE else View.VISIBLE
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
            setHasFixedSize(true)
        }
    }


}
