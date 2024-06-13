package com.dicoding.storysub.di

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.dicoding.storysub.data.api.AuthApiConfig
import com.dicoding.storysub.data.preferences.UserPreference
import com.dicoding.storysub.data.preferences.dataStore
import com.dicoding.storysub.data.repository.AuthRepository

object AuthInjection {
    fun provideRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = AuthApiConfig.getApiService(user.token)
        return AuthRepository(pref, apiService)
    }
}