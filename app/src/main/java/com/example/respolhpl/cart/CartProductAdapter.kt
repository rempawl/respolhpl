package com.example.respolhpl.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.cart.CartViewModel.CartItem
import com.example.respolhpl.databinding.ItemCartProductBinding
import com.example.respolhpl.databinding.ItemCartSummaryBinding
import com.example.respolhpl.utils.extensions.loadImage

class CartProductAdapter(private val onBuyClick: () -> Unit) :
    ListAdapter<CartItem, RecyclerView.ViewHolder>(diff) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CartItem.Product -> PRODUCT_TYPE
            is CartItem.Summary -> SUMMARY_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PRODUCT_TYPE -> CartProductViewHolder.from(parent)
            SUMMARY_TYPE -> CartSummaryViewHolder.from(parent, onBuyClick)
            else -> throw java.lang.IllegalStateException("wrong viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CartProductViewHolder -> holder.bind(getItem(position) as CartItem.Product)
            is CartSummaryViewHolder -> holder.bind(getItem(position) as CartItem.Summary)
        }
    }

    class CartProductViewHolder private constructor(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: CartItem.Product,
        ) {
            binding.apply {
//                dele.setOnClickListener { item.onDeleteClick() } todo
                price.text = "Cena za sztukę ${item.price} zł"
                quantity.text = "Ilośc: ${item.quantity}" // todo extract
                prodName.text = item.name
                thumbnail.loadImage(item.thumbnailSrc)
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

    class CartSummaryViewHolder private constructor(
        private val binding: ItemCartSummaryBinding,
        private val onBuyClick: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem.Summary) {
            with(binding) {
                cartCost.text = " Całkowity Koszt: ${item.cost} zł" // todo extract
                buyBtn.setOnClickListener { onBuyClick() }
            }
        }

        companion object {
            fun from(parent: ViewGroup, onBuyClick: () -> Unit): CartSummaryViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return CartSummaryViewHolder(
                    binding = ItemCartSummaryBinding.inflate(inflater, parent, false),
                    onBuyClick = onBuyClick
                )
            }
        }
    }


    private companion object {
        const val PRODUCT_TYPE = 1
        const val SUMMARY_TYPE = 2
        val diff by lazy {
            object : DiffUtil.ItemCallback<CartItem>() {
                override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                    return oldItem.itemId == newItem.itemId
                }

                override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}
