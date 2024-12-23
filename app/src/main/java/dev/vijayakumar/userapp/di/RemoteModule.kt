package dev.vijayakumar.userapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vijayakumar.userapp.repository.LoginRepository
import dev.vijayakumar.userapp.services.ApiServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

private val BASE_URL = "https://dummyjson.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }


    @Provides
    @Singleton
    fun provideRepository(apiServices:ApiServices):LoginRepository{
        return LoginRepository(apiServices)
    }

}