package com.example.respolhpl.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.R
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.databinding.ItemProductBinding
import com.example.respolhpl.utils.extensions.loadImage

typealias OnItemClickListener = (id: Int) -> Unit

class ProductListAdapter constructor(
    private val onItemClickListener: (id: Int) -> Unit
) : ListAdapter<ProductMinimal, ProductListAdapter.ProductViewHolder>(ProductDiff()) {


    class ProductViewHolder private constructor(
        private val binding: ItemProductBinding,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productMinimal: ProductMinimal) = binding.run {
            price.text =
                root.context.resources.getString(R.string.price, productMinimal.price) // todo ext
            prodName.text = productMinimal.name
            thumbnail.loadImage(productMinimal.thumbnailSrc)
            productCard.setOnClickListener { onItemClickListener(productMinimal.id) }
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: OnItemClickListener): ProductViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(inflater, parent, false)
                return ProductViewHolder(binding, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }
}
