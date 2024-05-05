package com.hossam.mobilemasrtask.product.domain.repository

import com.hossam.mobilemasrtask.product.data.dto.ProductDTO
import com.hossam.mobilemasrtask.product.data.dto.ProductsDTO
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow

interface IProductRepository {

    fun getProductsPaginated(
        offset: Int,
        limit: Int
    ): Flow<Result<List<ProductDTO>>>

    fun getProductDetail(productId: Int): Flow<Result<ProductDTO>>
}