package com.example.respolhpl.productDetails.imagesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.databinding.ItemImageBinding
import com.example.respolhpl.utils.extensions.loadImage

class ImagesAdapter constructor(
    private val bindingDecorator: ItemImageBinding.() -> Unit
) : ListAdapter<Image, ImagesAdapter.ImageViewHolder>(ImageDiffUtil()) {

    class ImageViewHolder private constructor(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(img: Image, count: String, decorate: ItemImageBinding.() -> Unit) {
            binding.apply {
                image.loadImage(img)
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
