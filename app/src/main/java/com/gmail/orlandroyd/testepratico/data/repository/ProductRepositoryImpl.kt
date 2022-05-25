package com.gmail.orlandroyd.testepratico.data.repository

import com.gmail.orlandroyd.testepratico.data.local.ProductDatabase
import com.gmail.orlandroyd.testepratico.domain.repository.ProductRepository
import com.gmail.orlandroyd.testepratico.domain.model.Product
import com.gmail.orlandroyd.testepratico.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor() : ProductRepository {

    override suspend fun fetchProducts(): Flow<DataState<List<Product>>> =
        flow {

            emit(DataState.success((ProductDatabase.getSampleList())))

        }.flowOn(Dispatchers.IO)

}
