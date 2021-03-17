package com.example.respolhpl.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.ProductMinimal
import com.example.respolhpl.databinding.FragmentHomeBinding
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var adapter: ProductListAdapter by autoCleared()
    private var binding: FragmentHomeBinding by autoCleared()

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
        return binding.root
    }


    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { res ->
            res.takeIf { it.isSuccess }?.let { submitProducts(res) }
        }
    }

    private fun submitProducts(res: Result<*>?) {
        @Suppress("UNCHECKED_CAST")
        res as Result.Success<List<ProductMinimal>>
        adapter.submitList(res.data)
    }

    private fun setupBinding() {
        binding.apply {
            productList.apply {
                adapter = this@HomeFragment.adapter
                layoutManager = chooseLayoutManager()
                setHasFixedSize(false)
            }
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun RecyclerView.chooseLayoutManager(): RecyclerView.LayoutManager =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            GridLayoutManager(context, 2)
        else LinearLayoutManager(context)
}