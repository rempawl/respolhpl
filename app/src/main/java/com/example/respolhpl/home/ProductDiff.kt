package com.example.respolhpl.home

import androidx.recyclerview.widget.DiffUtil
import com.example.respolhpl.data.product.ProductMinimal


class ProductDiff : DiffUtil.ItemCallback<ProductMinimal>() {
    override fun areItemsTheSame(oldItem: ProductMinimal, newItem: ProductMinimal): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ProductMinimal, newItem: ProductMinimal): Boolean {
        return oldItem == newItem
    }

}