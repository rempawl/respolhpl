package com.example.respolhpl.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var adapter: ProductListAdapter? = null
    private var binding: FragmentHomeBinding? = null

    private fun navigateToProductDetails(id: Int) {
        findNavController().navigate(
            HomeFragmentDirections.navigationHomeToProductDetails(id)
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        adapter = ProductListAdapter(onItemClickListener = { id -> navigateToProductDetails(id) })

         binding = FragmentHomeBinding.inflate(inflater)
        setupBinding()
        setupObservers()

        return binding?.root ?: throw IllegalStateException("Binding should be initialized")
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        binding = null
    }
    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            res.takeIf { it.isSuccess }?.let { submitProducts(res) }
        }
    }

    private fun submitProducts(res: Result<*>?) {
        @Suppress("UNCHECKED_CAST")
        res as Result.Success<List<Product>>
        adapter?.submitList(res.data)
    }

    private fun setupBinding() {
        binding?.apply {
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