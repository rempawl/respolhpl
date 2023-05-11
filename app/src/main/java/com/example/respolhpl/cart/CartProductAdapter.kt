package com.example.respolhpl.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.cart.data.CartItem
import com.example.respolhpl.databinding.ItemCartProductBinding
import com.example.respolhpl.databinding.ItemCartSummaryBinding
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartProductAdapter(
    private val dispatchersProvider: DispatchersProvider,
    private val onDeleteClickListener: (CartItem.CartProduct) -> Unit
) : ListAdapter<CartItem, RecyclerView.ViewHolder>(Diff()) {
    companion object {
        const val PRODUCT_TYPE = 1
        const val SUMMARY_TYPE = 2
    }


    fun createSummaryAndSubmitList(list: List<CartItem.CartProduct>) {
        CoroutineScope(dispatchersProvider.default + Job()).launch {
            if (list.isEmpty()) {
                submitItems(list)
                return@launch
            }
            val summary = createSummary(list)
            val items = list + summary
            submitItems(items)
        }

    }

    private suspend fun submitItems(items: List<CartItem>) {
        withContext(dispatchersProvider.main) {
            submitList(items)
        }
    }

    private fun createSummary(list: List<CartItem.CartProduct>): CartItem.Summary {
        return CartItem.Summary(cost = list.sumOf {
            it.cost
        })
    }


    class CartProductViewHolder private constructor(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: CartItem.CartProduct,
            onDeleteClickListener: (CartItem.CartProduct) -> Unit
        ) {
            binding.apply {
//                product = item
                deleteBtn.setOnClickListener { onDeleteClickListener(item) }
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

    class CartSummaryViewHolder private constructor(private val binding: ItemCartSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem.Summary) {
            binding.cartCost.text = "Koszt: ${String.format("%.2f", item.cost)} zł"
        }

        companion object {
            fun from(parent: ViewGroup): CartSummaryViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return CartSummaryViewHolder(
                    ItemCartSummaryBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is CartItem.CartProduct -> PRODUCT_TYPE
            is CartItem.Summary -> SUMMARY_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PRODUCT_TYPE -> CartProductViewHolder.from(parent)
            SUMMARY_TYPE -> CartSummaryViewHolder.from(parent)
            else -> throw java.lang.IllegalStateException("wrong viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CartProductViewHolder -> holder.bind(
                getItem(position) as CartItem.CartProduct,
                onDeleteClickListener
            )
            is CartSummaryViewHolder -> holder.bind(getItem(position) as CartItem.Summary)
        }
    }

    class Diff : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

    }
}
