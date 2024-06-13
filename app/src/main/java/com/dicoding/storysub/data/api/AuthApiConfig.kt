package com.dicoding.storysub.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.dicoding.storysub.BuildConfig

class AuthApiConfig {
    companion object {
        fun getApiService(token: String): AuthApiService {
            val baseUrl = BuildConfig.BASE_URL
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(modifiedRequest)
            }
            val client = OkHttpClient.Builder().run {
                addInterceptor(loggingInterceptor)
                addInterceptor(authInterceptor)
                build()
            }
            val retrofit = Retrofit.Builder().apply {
                baseUrl(baseUrl)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
            }.build()
            return retrofit.create(AuthApiService::class.java)
        }
    }
}
