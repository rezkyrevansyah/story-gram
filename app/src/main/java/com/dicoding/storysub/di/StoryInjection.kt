package com.dicoding.storysub.di

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.dicoding.storysub.data.api.StoryApiConfig
import com.dicoding.storysub.data.preferences.UserPreference
import com.dicoding.storysub.data.preferences.dataStore
import com.dicoding.storysub.data.repository.StoryRepository

object StoryInjection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = StoryApiConfig.getApiService(user.token)
        return StoryRepository(pref, apiService)
    }
}