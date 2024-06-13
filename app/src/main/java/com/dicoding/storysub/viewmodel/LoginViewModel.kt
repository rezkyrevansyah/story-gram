package com.dicoding.storysub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(name: String, email: String) = repository.login(name, email)
}