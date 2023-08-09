package com.example.respolhpl.productDetails.imagesAdapter


import androidx.recyclerview.widget.DiffUtil
import com.example.respolhpl.data.model.domain.Image

class ImageDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}