package com.example.respolhpl.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respolhpl.R
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.Result
import com.example.respolhpl.databinding.CartFragmentBinding
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private val viewModel: CartViewModel by viewModels()
    private var adapter by autoCleared<CartProductAdapter>()
    private var binding by autoCleared<CartFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CartFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartProductAdapter { prod -> onDeleteBtnClick(prod) }
        binding.setupBinding()
        setupObservers()

    }

    private fun onDeleteBtnClick(prod: CartProduct) {
        ConfirmDialog.newInstance(getString(R.string.deletion_confirmation_title)) {
            viewModel.deleteFromCart(prod)
        }.show(childFragmentManager, "")
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            updateAdapterList(res)
        }
    }

    private fun updateAdapterList(res: Result<*>) {
        res.checkIfIsSuccessAndListOf<CartProduct>()?.let { prods ->
            adapter.submitList(prods)
        }
    }


    private fun CartFragmentBinding.setupBinding() {
        setupProductsListAdapter()
        backBtn.setOnClickListener { findNavController().navigateUp() }
        clearBtn.setOnClickListener {
            ConfirmDialog.newInstance(getString(R.string.confirm_clear_cart)) {
                this@CartFragment.viewModel.clearCart()
            }.show(childFragmentManager,"")
        }
        label.setText(R.string.cart)
        lifecycleOwner = viewLifecycleOwner
        viewModel = this@CartFragment.viewModel
    }

    private fun CartFragmentBinding.setupProductsListAdapter() {
        prodsList.apply {
            adapter = this@CartFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }


}