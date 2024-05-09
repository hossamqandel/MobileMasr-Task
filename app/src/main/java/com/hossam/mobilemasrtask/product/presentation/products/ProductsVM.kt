package com.hossam.mobilemasrtask.product.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.product.domain.mapper.toProduct
import com.hossam.mobilemasrtask.product.domain.repository.IProductRepository
import com.hossam.mobilemasrtask.product.presentation.products.util.ProductsState
import com.hossam.mobilemasrtask.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing and providing data related to products.
 * @param repo The repository responsible for fetching product data.
 */
@HiltViewModel
class ProductsVM @Inject constructor(
    private val repo: IProductRepository
) : ViewModel() {

    private var getProductsJob: Job? = null

    // SharedFlow to emit the state of products data to observing components.
    private val _state = MutableSharedFlow<ProductsState>(replay = 2)
    val state = _state.asSharedFlow()

    private val _errorState = MutableSharedFlow<Int>()
    val errorState = _errorState.asSharedFlow()

    // Offset and limit for pagination.
    private var offset = 0
    private var limit = 10

    init {
        // Fetch products when ViewModel is initialized.
        getProducts()
    }

    /**
     * Fetches products from the repository.
     */
    private fun getProducts() {
        getProductsJob?.cancel() // Cancels previous job to avoid overlapping requests.
        getProductsJob = viewModelScope.launch {
            repo.getProductsPaginated(offset, limit).collectLatest { result ->
                when (result) {
                    is Result.Loading -> submitState(ProductsState.Loading(true))
                    is Result.Success -> {
                        submitState(ProductsState.Loading(false))
                        if (result.data.isNullOrEmpty()) {
                            submitState(ProductsState.NoProducts)
                        } else {
                            // Maps product DTOs to domain model and emits success state.
                            val asProductsSet = result.data.map { it.toProduct() }.toSet()
                            submitState(ProductsState.Success(asProductsSet))
                        }
                    }

                    is Result.Error -> {
                        submitState(ProductsState.Loading(false))
                        result.message?.let {
                            _errorState.emit(result.message)
                        }
                    }
                }
            }
        }
    }

    /**
     * Refreshes products by re-fetching from the repository.
     */
    fun refreshProducts() {
        getProducts()
    }

    /**
     * Emits a state to the shared flow.
     * @param state The state to emit.
     */
    private suspend fun submitState(state: ProductsState) {
        _state.emit(state)
    }

    /**
     * Increases the offset for pagination.
     */
    fun upgradePage() {
        offset += limit
    }

    /**
     * Cancels the job when ViewModel is cleared.
     */
    override fun onCleared() {
        getProductsJob?.cancel()
        super.onCleared()
    }

}
