package com.hossam.mobilemasrtask.product.data.repository

import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.common.data.remote.WebService
import com.hossam.mobilemasrtask.product.data.dto.ProductDTO
import com.hossam.mobilemasrtask.product.data.dto.toProductEntity
import com.hossam.mobilemasrtask.product.data.local.ProductDAO
import com.hossam.mobilemasrtask.product.data.local.toProductDTO
import com.hossam.mobilemasrtask.product.domain.repository.IProductRepository
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: WebService,
    private val dao: ProductDAO
) : IProductRepository {

    override fun getProductsPaginated(
        offset: Int,
        limit: Int
    ): Flow<Result<List<ProductDTO>>> = flow {

        emit(Result.Loading())

        try {

            val result = api.getProductsPaginated(offset, limit)

            when (result.code()) {

                HttpURLConnection.HTTP_OK -> {
                    val products = result.body()
                    products?.let { productsDTO ->
                        dao.addProducts(productsDTO.map { it.toProductEntity() })
                        emit(Result.Success(dao.getProducts().map { it.toProductDTO() }))
                    }
                }

                HttpURLConnection.HTTP_NO_CONTENT -> {
                    emit(Result.Success(emptyList()))
                }

                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    emit(Result.Error(R.string.error_unauthorized))
                }

                HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                    emit(Result.Error(R.string.error_http_internal_error))
                }

            }

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Success(dao.getProducts().map { it.toProductDTO() }))
            emit(Result.Error(R.string.error_no_internet))
        }

    }

    override fun getProductDetail(productId: Int): Flow<Result<ProductDTO>> = flow {

        emit(Result.Loading())

        try {

            val result = api.getProductById(productId)

            when (result.code()) {

                HttpURLConnection.HTTP_OK -> {
                    val productDetail = result.body()
                    productDetail?.let { productDTO ->
                        dao.addProduct(productDTO.toProductEntity())
                        emit(Result.Success(productDTO))
                    }
                }

                HttpURLConnection.HTTP_BAD_REQUEST ->
                    emit(Result.Error(R.string.error_bad_request_item_not_available))

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Result.Error(R.string.error_unauthorized))

                HttpURLConnection.HTTP_INTERNAL_ERROR ->
                    emit(Result.Error(R.string.error_http_internal_error))

            }

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Error(R.string.error_no_internet))
        }
    }


}