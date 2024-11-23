package com.rempawl.respolhpl.productDetails.imagesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rempawl.respolhpl.data.model.domain.ProductImage
import com.rempawl.respolhpl.databinding.ItemImageBinding
import com.rempawl.respolhpl.utils.extensions.loadImage

class ImagesAdapter(
    private val bindingDecorator: ItemImageBinding.() -> Unit = {}
) : ListAdapter<ProductImage, ImagesAdapter.ImageViewHolder>(ImageDiffUtil()) {

    class ImageViewHolder private constructor(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(img: ProductImage, decorate: ItemImageBinding.() -> Unit) {
            binding.apply {
                image.loadImage(img)
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
        holder.bind(getItem(position), bindingDecorator)
    }
}
