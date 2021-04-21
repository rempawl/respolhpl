package com.example.respolhpl.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        setHasOptionsMenu(true)
        binding = CartFragmentBinding.inflate(inflater)
        adapter = CartProductAdapter()
        setupBinding()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner){ res ->
            updateAdapterList(res)
        }
    }

    private fun updateAdapterList(res: Result<*>) {
        res.checkIfIsSuccessAndListOf<CartProduct>()?.let { prods ->
            adapter.submitList(prods)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.cart_fragment).isVisible = false
    }

    private fun setupBinding() {
        binding.apply {
            setupProductsListAdapter()
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@CartFragment.viewModel
        }
    }

    private fun CartFragmentBinding.setupProductsListAdapter() {
        prodsList.apply {
            adapter = this@CartFragment.adapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }



}