package com.hossam.mobilemasrtask.product.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.product.domain.mapper.toProduct
import com.hossam.mobilemasrtask.product.domain.repository.IProductRepository
import com.hossam.mobilemasrtask.product.presentation.products.util.ProductsState
import com.hossam.mobilemasrtask.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsVM @Inject constructor(
    private val repo: IProductRepository
) : ViewModel() {


    private var getProductsJob: Job? = null

    private val _state = MutableSharedFlow<ProductsState>(replay = 2)
    val state = _state.asSharedFlow()

    private var offset = 0
    private var limit = 10

    fun getProducts() {
        getProductsJob?.cancel()
        getProductsJob = viewModelScope.launch {
            repo.getProductsPaginated(offset, limit).collectLatest { result ->
                when (result) {
                    is Result.Loading -> submitState(ProductsState.Loading(true))
                    is Result.Success -> {
                        submitState(ProductsState.Loading(false))
                        if (result.data.isNullOrEmpty()) {
                            submitState(ProductsState.NoProducts)
                        } else {
                            val asProductsSet = result.data.map { it.toProduct() }.toSet()
                            submitState(ProductsState.Success(asProductsSet))
                        }
                    }

                    is Result.Error -> {
                        submitState(ProductsState.Loading(false))
                        result.message?.let {
                            submitState(ProductsState.Error(result.message))
                        }
                    }
                }
            }
        }
    }

    private suspend fun submitState(state: ProductsState) {
        _state.emit(state)
    }

    fun upgradePage(){
        offset += limit
    }

    override fun onCleared() {
        getProductsJob?.cancel()
        super.onCleared()
    }


}