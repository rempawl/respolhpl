package com.example.respolhpl.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.respolhpl.databinding.FragmentHomeBinding
import com.example.respolhpl.utils.event.EventObserver
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



@AndroidEntryPoint
class HomeFragment : Fragment()  {

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
        setHasOptionsMenu(true)
        setupBinding()
        setupObservers()
        initAdapter()
        return binding.root
    }


    private fun initAdapter() {
        binding.productList.adapter = adapter.withLoadStateFooter(
            footer = ProductLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            binding.productList.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.error.rootView.isVisible = loadState.source.refresh is LoadState.Error
            binding.loading.progressView.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result?.collect {
                adapter.submitData(it)
            }
        }
        viewModel.shouldNavigate.observe(viewLifecycleOwner, EventObserver { id ->
            navigateToProductDetails(id)
        })


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
            error.retryButton.setOnClickListener { adapter.retry() }
        }
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