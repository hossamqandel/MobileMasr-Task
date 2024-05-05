package com.hossam.mobilemasrtask.product.data.dto

import com.hossam.mobilemasrtask.product.data.local.ProductEntity

data class ProductDTO(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val images: Set<String>,
    val category: CategoryDTO
)

data class CategoryDTO(
    val id: Int,
    val image: String,
    val name: String
)

fun ProductDTO.toProductEntity() = ProductEntity (
    id = id,
    title = title,
    price = price,
    description = description,
    images = images,
    category = category,
)