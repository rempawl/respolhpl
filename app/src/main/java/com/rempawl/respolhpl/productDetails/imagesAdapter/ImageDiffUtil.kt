package com.rempawl.respolhpl.productDetails.imagesAdapter


import androidx.recyclerview.widget.DiffUtil
import com.rempawl.respolhpl.data.model.domain.ProductImage

class ImageDiffUtil : DiffUtil.ItemCallback<ProductImage>() {

    override fun areItemsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
        return oldItem == newItem
    }
}