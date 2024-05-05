package com.hossam.mobilemasrtask.product.presentation.product_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hossam.mobilemasrtask.databinding.ItemProductImageBinding
import com.hossam.mobilemasrtask.util.extension.loadWithGlide
import java.util.Collections

class ProductImageAdapter : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    private var images: Set<String> = Collections.emptySet()

    fun setImages(images: Set<String>) {
        this.images = images
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = images.elementAtOrNull(position) ?: return
        holder.bind(product)

    }

    override fun getItemCount(): Int = images.size


    class ViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String?) {
            with(binding) {
                imageUrl?.let { ivProductImage.loadWithGlide(imageUrl) }
            }
        }
    }

}