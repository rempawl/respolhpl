package com.example.respolhpl.productDetails.imagesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.databinding.ItemImageBinding

class ImagesAdapter constructor(
    private val bindingDecorator: ItemImageBinding.() -> Unit
) : ListAdapter<Image, ImagesAdapter.ImageViewHolder>(ImageDiffUtil()) {

    class ImageViewHolder private constructor(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image, count: String, decorate: ItemImageBinding.() -> Unit) {
            binding.apply {
                img = image
                imgCounter.text = count
                decorate()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                return ImageViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position), "${position + 1}/$itemCount", bindingDecorator)
    }


}
