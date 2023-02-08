package com.caspar.cpdemo.di

import com.caspar.cpdemo.di.domain.HomeRepository
import com.caspar.cpdemo.di.domain.impl.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
}