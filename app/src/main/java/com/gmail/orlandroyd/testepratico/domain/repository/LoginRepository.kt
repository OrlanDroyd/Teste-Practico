package com.gmail.orlandroyd.testepratico.domain.repository

import com.gmail.orlandroyd.testepratico.domain.model.User
import com.gmail.orlandroyd.testepratico.util.DataState
import kotlinx.coroutines.flow.Flow


interface LoginRepository {

    suspend fun login(username: String, password: String): Flow<DataState<User>>

}