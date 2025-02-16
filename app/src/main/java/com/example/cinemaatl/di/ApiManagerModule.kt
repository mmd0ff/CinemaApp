package com.example.cinemaatl.di

import com.example.cinemaatl.ApiInterceptorKey
import com.example.cinemaatl.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object ApiManagerModule {

    @Provides
    fun provideHttpLoggingInterceptor():HttpLoggingInterceptor{
        val level = HttpLoggingInterceptor.Level.BODY
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = level
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        apiInterceptorKey: ApiInterceptorKey,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptorKey)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory

    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }
    @Provides
    fun provideCService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }



}