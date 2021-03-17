package com.example.respolhpl.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.data.product.ProductMinimal
import com.example.respolhpl.databinding.ProductItemBinding

typealias OnItemClickListener = (id: Int) -> Unit

class ProductListAdapter constructor(
    private val onItemClickListener: (id: Int) -> Unit
) : ListAdapter<ProductMinimal, ProductListAdapter.ProductViewHolder>(ProductDiff()) {


    class ProductViewHolder private constructor(
        private val binding: ProductItemBinding,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productMinimal: ProductMinimal) {
            binding.product = productMinimal
            binding.productCard.setOnClickListener { onItemClickListener(productMinimal.id) }
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: OnItemClickListener): ProductViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ProductItemBinding.inflate(inflater, parent, false)

                return ProductViewHolder(binding, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}