package com.example.respolhpl.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.databinding.ItemCartProductBinding

//todo cart summary
class CartProductAdapter(private val onDeleteClickListener: (CartProduct) -> Unit) :
    ListAdapter<CartProduct, RecyclerView.ViewHolder>(Diff()) {
    companion object {
        const val PRODUCT_TYPE = 1
        const val SUMMARY_TYPE = 2
    }


    class CartProductViewHolder private constructor(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartProduct, onDeleteClickListener: (CartProduct) -> Unit) {
            binding.apply {
                this.product = product
                deleteBtn.setOnClickListener { onDeleteClickListener(product) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): CartProductViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return CartProductViewHolder(
                    ItemCartProductBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item is CartProduct) {
            return PRODUCT_TYPE
        } else {
            throw IllegalStateException("Item type is wrong : ${item.javaClass}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PRODUCT_TYPE -> CartProductViewHolder.from(parent)
            else -> throw java.lang.IllegalStateException("wrong viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartProductViewHolder) {
            holder.bind(getItem(position), onDeleteClickListener)
        }
    }

    class Diff : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
}