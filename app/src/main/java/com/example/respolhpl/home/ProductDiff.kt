package com.example.respolhpl.home

import androidx.recyclerview.widget.DiffUtil
import com.example.respolhpl.data.model.domain.ProductMinimal


class ProductDiff : DiffUtil.ItemCallback<ProductMinimal>() {
    override fun areItemsTheSame(oldItem: ProductMinimal, newItem: ProductMinimal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductMinimal, newItem: ProductMinimal): Boolean {
        return oldItem == newItem
    }

}
