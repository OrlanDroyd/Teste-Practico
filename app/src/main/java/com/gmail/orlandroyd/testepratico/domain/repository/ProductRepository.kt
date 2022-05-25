package com.gmail.orlandroyd.testepratico.domain.repository

import com.gmail.orlandroyd.testepratico.domain.model.Product
import com.gmail.orlandroyd.testepratico.util.DataState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun fetchProducts(): Flow<DataState<List<Product>>>

}