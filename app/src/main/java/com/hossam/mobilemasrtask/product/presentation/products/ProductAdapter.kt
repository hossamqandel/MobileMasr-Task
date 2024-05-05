package com.hossam.mobilemasrtask.product.presentation.products

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.databinding.ItemProductBinding
import com.hossam.mobilemasrtask.product.domain.model.Product
import com.hossam.mobilemasrtask.util.extension.loadWithGlide
import com.hossam.mobilemasrtask.util.extension.onClick
import java.util.Collections

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products: Set<Product> = Collections.emptySet()

    fun setProducts(products: Set<Product>) {
        this.products = products
    }


    private var onProductClickEvent: (((product: Product) -> Unit))? = null

    fun setOnProductClickEvent(onProductClickEvent: (((product: Product) -> Unit))) {
        this.onProductClickEvent = onProductClickEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = products.elementAtOrNull(position) ?: return
        holder.bind(product)
        holder.binding.root.onClick {
            onProductClickEvent?.invoke(product)
        }
    }

    override fun getItemCount(): Int = products.size


    class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            with(binding) {
                tvProductTitle.text = product.title
                tvProductPrice.text = "${product.price} ${binding.root.context.getString(R.string.egp)}"
                tvProductDescription.text = product.description
                val url = product.images.firstOrNull()
                url?.let { ivProductImage.loadWithGlide(url) }
            }
        }
    }


}