package com.gmail.orlandroyd.testepratico.di

import com.gmail.orlandroyd.testepratico.data.repository.LoginRepositoryImpl
import com.gmail.orlandroyd.testepratico.data.repository.ProductRepositoryImpl
import com.gmail.orlandroyd.testepratico.domain.repository.LoginRepository
import com.gmail.orlandroyd.testepratico.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoginRepository(): LoginRepository = LoginRepositoryImpl()

    @Singleton
    @Provides
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

}