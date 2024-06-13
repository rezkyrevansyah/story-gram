package com.dicoding.storysub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.repository.StoryRepository
import com.dicoding.storysub.data.response.ListStoryItem

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}