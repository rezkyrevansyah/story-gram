package com.dicoding.storysub.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storysub.data.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = repository.register(name, email, password)
}