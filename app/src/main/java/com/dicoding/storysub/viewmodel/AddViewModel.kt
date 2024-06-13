package com.dicoding.storysub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.dicoding.storysub.data.repository.StoryRepository
import java.io.File

class AddViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
        }
    }

    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}