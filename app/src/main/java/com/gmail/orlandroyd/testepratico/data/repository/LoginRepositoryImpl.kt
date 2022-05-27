package com.gmail.orlandroyd.testepratico.data.repository

import com.gmail.orlandroyd.testepratico.domain.model.User
import com.gmail.orlandroyd.testepratico.domain.repository.LoginRepository
import com.gmail.orlandroyd.testepratico.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor() : LoginRepository {

    // Only a placeholder to simulate login action
    override suspend fun login(username: String, password: String): Flow<DataState<User>> = flow {
        if (username == "admin" && password == "admin") {
            emit(
                DataState.success(
                    User(
                        username = "admin",
                        password = "admin",
                        status = "success"
                    )
                )
            )
        } else {
            emit(
                DataState.error("Invalid credentials")
            )
        }

    }.flowOn(Dispatchers.IO)
}
