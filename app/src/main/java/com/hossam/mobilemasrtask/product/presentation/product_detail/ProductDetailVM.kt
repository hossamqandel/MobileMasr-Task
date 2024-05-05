package com.hossam.mobilemasrtask.product.presentation.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.product.domain.mapper.toProduct
import com.hossam.mobilemasrtask.product.domain.repository.IProductRepository
import com.hossam.mobilemasrtask.product.presentation.product_detail.util.ProductDetailState
import com.hossam.mobilemasrtask.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailVM @Inject constructor(
    private val repo: IProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var productId: Int = -1

    init {
        savedStateHandle.get<Int>("productId")?.let { productId ->
            this.productId = productId
        }
    }


    private val _state = MutableSharedFlow<ProductDetailState>(replay = 2)
    val state = _state.asSharedFlow()

    fun getProductDetail() {
        viewModelScope.launch {
            if (productId!= -1){
                repo.getProductDetail(productId).collectLatest { result ->
                    when (result) {
                        is Result.Loading -> submitState(ProductDetailState.Loading(true))

                        is Result.Success -> {
                            submitState(ProductDetailState.Loading(false))
                            result.data?.let { productDto ->
                                submitState(ProductDetailState.Success(productDto.toProduct()))
                            }
                        }

                        is Result.Error -> {
                            submitState(ProductDetailState.Loading(false))
                            result.message?.let {
                                submitState(ProductDetailState.Error(result.message))
                            }
                        }
                    }

                }
            }
        }
    }


    private suspend fun submitState(state: ProductDetailState) {
        _state.emit(state)
    }

}