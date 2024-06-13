package com.dicoding.storysub.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storysub.data.repository.AuthRepository
import com.dicoding.storysub.di.AuthInjection
import com.dicoding.storysub.viewmodel.LoginViewModel
import com.dicoding.storysub.viewmodel.RegisterViewModel

class AuthViewModelFactory(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            when {
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(repository) as T
                }

                modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                    RegisterViewModel(repository) as T
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        } catch (e: Exception) {
            throw RuntimeException(e) // Wrap any exceptions to avoid direct handling
        }
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(AuthInjection.provideRepository(context)).also {
                    instance = it
                }
            }
        }
    }
}
