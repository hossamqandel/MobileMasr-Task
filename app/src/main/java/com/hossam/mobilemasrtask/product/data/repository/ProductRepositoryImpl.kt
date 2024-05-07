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
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

/**
 * Implementation of the [IProductRepository] interface.
 * This repository fetches products from a remote web service [api] and stores them locally using a [dao].
 * @property api The web service instance used to fetch products remotely.
 * @property dao The Data Access Object (DAO) used to store products locally.
 */
class ProductRepositoryImpl @Inject constructor(
    private val api: WebService,
    private val dao: ProductDAO
) : IProductRepository {

    /**
     * Retrieves a paginated list of products.
     * @param offset The offset for pagination.
     * @param limit The maximum number of products to retrieve.
     * @return A flow emitting [Result] objects containing the list of products.
     */
    override fun getProductsPaginated(
        offset: Int,
        limit: Int
    ): Flow<Result<List<ProductDTO>>> = flow {

        emit(Result.Loading()) // Emit loading state before starting the api call.

        try {
            // Fetch products from the remote API.
            val result = api.getProductsPaginated(offset, limit)
            val statusCode = result.code()

            when (statusCode) {
                HTTP_OK -> {
                    // If request was successful, parse the response body.
                    val products = result.body()
                    products?.let { productsDTO ->
                        // Save products to local database.
                        dao.addProducts(productsDTO.map { it.toProductEntity() })
                        // Emit success state with the list of products fetched from local database.
                        emit(Result.Success(dao.getProducts().map { it.toProductDTO() }))
                    }
                }

                HTTP_NO_CONTENT ->
                    // If no content found, emit success state with an empty list.
                    emit(Result.Success(emptyList()))


                HTTP_UNAUTHORIZED ->
                    // If unauthorized, emit error state with appropriate message resource.
                    emit(Result.Error(R.string.error_unauthorized))


                HTTP_INTERNAL_ERROR ->
                    // If internal server error, emit error state with appropriate message resource.
                    emit(Result.Error(R.string.error_http_internal_error))

            }

        } catch (e: IOException) {
            // If an IO exception occurs (probably no internet connection),
            // emit success state with the products fetched from local database,
            // and emit error state indicating the lack of internet connection.
            e.printStackTrace()
            emit(Result.Success(dao.getProducts().map { it.toProductDTO() }))
            emit(Result.Error(R.string.error_no_internet))
        }
    }

    override fun getProductDetail(productId: Int): Flow<Result<ProductDTO>> = flow {

        emit(Result.Loading())

        try {

            val result = api.getProductById(productId)
            val statusCode = result.code()
            when (statusCode) {

                HTTP_OK -> result.body()?.let { productDTO ->
                    dao.addProduct(productDTO.toProductEntity())
                    emit(Result.Success(productDTO))
                }

                HTTP_BAD_REQUEST or HTTP_NOT_FOUND ->
                    emit(Result.Error(R.string.error_item_not_available))

                HTTP_UNAUTHORIZED ->
                    emit(Result.Error(R.string.error_unauthorized))

                HTTP_INTERNAL_ERROR ->
                    emit(Result.Error(R.string.error_http_internal_error))

            }

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Error(R.string.error_no_internet))
        }
    }


}