package com.example.respolhpl.utils


import androidx.recyclerview.widget.DiffUtil
import com.example.respolhpl.data.product.Image

class ImageDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

}