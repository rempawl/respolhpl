package com.example.respolhpl.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.databinding.ProductLoadStateViewItemBinding

class ProductLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ProductLoadStateAdapter.ProductLoadStateViewHolder>() {

    class ProductLoadStateViewHolder(
        private val binding: ProductLoadStateViewItemBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.error.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                loading.progressView.isVisible = loadState is LoadState.Loading
                error.rootView.isVisible = loadState is LoadState.Error
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ProductLoadStateViewHolder {

                val binding = ProductLoadStateViewItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return ProductLoadStateViewHolder(binding, retry)
            }
        }
    }

    override fun onBindViewHolder(holder: ProductLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ProductLoadStateViewHolder {
        return ProductLoadStateViewHolder.create(parent, retry)
    }
}
