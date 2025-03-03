package com.example.cinemaatl.common

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptorKey @Inject constructor():Interceptor {
    private val key = "JX6MD15-J8N43ZY-P3JHP1D-77PGX0Q"


    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", key)
            .build()

        val newRequest = originalRequest.newBuilder()
            .addHeader("X-API-KEY",key)
            .build()

        return chain.proceed(newRequest)

    }
}