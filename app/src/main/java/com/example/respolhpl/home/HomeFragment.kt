package com.example.respolhpl.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.R
import com.example.respolhpl.databinding.FragmentHomeBinding
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


//todo category filters
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var adapter: ProductListAdapter by autoCleared()
    private var binding: FragmentHomeBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = ProductListAdapter(onItemClickListener = { id -> viewModel.navigate(id) })
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupBinding()
        setupObservers()
        binding.initAdapter()
    }


    private fun FragmentHomeBinding.initAdapter() {
        productList.apply {
            layoutManager = chooseLayoutManager()
            setHasFixedSize(false)
        }
//        productList.adapter = adapter.withLoadStateFooter(
//            footer = ProductLoadStateAdapter { adapter.retry() }
//        )
//        adapter.addLoadStateListener { loadState ->
//            productList.isVisible = loadState.source.refresh is LoadState.NotLoading
//            error.rootView.isVisible = loadState.source.refresh is LoadState.Error
//            loading.progressView.isVisible = loadState.source.refresh is LoadState.Loading
//        }
    }


    private fun setupObservers() = with(viewLifecycleOwner.lifecycleScope) {
        this.launch {
//            viewModel.items.collectLatest { adapter.submitData(it) }
        }

        viewModel.shouldNavigate
            .map { it.id }
            .onEach { id -> navigateToProductDetails(id) }
            .launchIn(this)
    }

    private fun FragmentHomeBinding.setupBinding() {
        toolbar.apply {
            label.text = getString(R.string.label_main)
            cartBtn.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionNavHomeToCartFragment()) }
        }
//        error.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun navigateToProductDetails(id: Int) {
        findNavController().navigate(
            HomeFragmentDirections.navigationHomeToProductDetails(id)
        )
    }

    private fun RecyclerView.chooseLayoutManager(): RecyclerView.LayoutManager =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            GridLayoutManager(context, 2)
        else LinearLayoutManager(context)
}
