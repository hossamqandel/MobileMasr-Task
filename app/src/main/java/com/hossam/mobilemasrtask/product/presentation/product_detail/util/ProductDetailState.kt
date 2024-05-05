package com.hossam.mobilemasrtask.product.presentation.product_detail.util

import com.hossam.mobilemasrtask.product.domain.model.Product

sealed interface ProductDetailState {

    data class Loading(val isLoading: Boolean): ProductDetailState

    data class Success(val product: Product): ProductDetailState

    data class Error(val message: Int): ProductDetailState
}