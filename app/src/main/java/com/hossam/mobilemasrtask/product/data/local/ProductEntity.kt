package com.hossam.mobilemasrtask.product.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hossam.mobilemasrtask.product.data.dto.CategoryDTO
import com.hossam.mobilemasrtask.product.data.dto.ProductDTO

@Entity(tableName = "products")
data class ProductEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val images: Set<String>,
    val category: CategoryDTO,
)

fun ProductEntity.toProductDTO() = ProductDTO(
    id = id,
    title = title,
    price = price,
    description = description,
    images = images,
    category = category
)
