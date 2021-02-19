package com.example.respolhpl.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: ProductListAdapter by lazy {
        ProductListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentHomeBinding.inflate(inflater)
        setupBinding(binding)
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.data.observe(viewLifecycleOwner) { res ->
            res.takeIf { it.isSuccess }?.let { submitProducts(res) }
        }
    }

    private fun submitProducts(res: Result<*>?) {
        @Suppress("UNCHECKED_CAST")
        res as Result.Success<Product>
        adapter.submitList(listOf(res.data))
    }

    private fun setupBinding(binding: FragmentHomeBinding) {
        binding.apply {
            productList.apply {
                adapter = this@HomeFragment.adapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(false)
            }
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

        }
    }
}