package com.hossam.mobilemasrtask.product.presentation.products.util

import com.hossam.mobilemasrtask.product.domain.model.Product

sealed interface ProductsState {

    data class Loading (val isLoading: Boolean) : ProductsState
    data class Success(val products: Set<Product>) : ProductsState
    data object NoProducts : ProductsState

}