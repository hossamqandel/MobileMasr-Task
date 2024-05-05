package com.hossam.mobilemasrtask.product.domain.model

import com.hossam.mobilemasrtask.product.data.dto.CategoryDTO

data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val images: Set<String>,
    val category: CategoryDTO
)


