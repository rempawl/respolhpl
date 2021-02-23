package com.example.respolhpl.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.respolhpl.data.product.Product


class ProductDiff : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}