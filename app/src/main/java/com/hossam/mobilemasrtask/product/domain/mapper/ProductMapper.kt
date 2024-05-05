package com.hossam.mobilemasrtask.product.domain.mapper

import com.hossam.mobilemasrtask.product.data.dto.ProductDTO
import com.hossam.mobilemasrtask.product.domain.model.Product

fun ProductDTO.toProduct() = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    images = images,
    category = category
)