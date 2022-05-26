package com.gmail.orlandroyd.testepratico.presentation.my_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.orlandroyd.testepratico.domain.model.Product
import com.gmail.orlandroyd.testepratico.domain.repository.ProductRepository
import com.gmail.orlandroyd.testepratico.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject
constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<DataState<List<Product>>>(DataState())
    val products: StateFlow<DataState<List<Product>>> = _products

    init {
        viewModelScope.launch {
            productRepository.fetchProducts()
                .catch { e ->
                    delay(1000) // Only for test
                    _products.emit(DataState.error(e.toString()))
                }
                .collect {
                    delay(1000) // Only for test
                    _products.emit(it)
                }
        }
    }

}