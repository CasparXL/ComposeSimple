package com.caspar.cpdemo.di

import android.content.Context
import com.caspar.cpdemo.network.Api
import com.caspar.cpdemo.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @BodyOkHttpClient
    @Provides
    fun provideBodyApi(): ApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //添加ScalarsConverterFactory支持
            .addConverterFactory(GsonConverterFactory.create())//可以接收自定义的Gson，当然也可以不传
            .client(Api.okhttpLogBody)
            .build()
            .create()
    }

    @HeaderOkHttpClient
    @Provides
    fun provideHeaderApi(): ApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //添加ScalarsConverterFactory支持
            .addConverterFactory(GsonConverterFactory.create())//可以接收自定义的Gson，当然也可以不传
            .client(Api.okhttpHeader)
            .build()
            .create()
    }

}