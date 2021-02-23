package com.example.respolhpl.productDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.data.product.Image
import com.example.respolhpl.databinding.ImageItemBinding
import com.example.respolhpl.utils.ImageDiffUtil
import javax.inject.Inject

class ImagesAdapter @Inject constructor() : ListAdapter<Image, ImagesAdapter.ImageViewHolder>(ImageDiffUtil()) {

    class ImageViewHolder private constructor(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            binding.img = image
        }

        companion object {
            fun from(parent: ViewGroup): ImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ImageItemBinding.inflate(inflater, parent, false)
                return ImageViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
