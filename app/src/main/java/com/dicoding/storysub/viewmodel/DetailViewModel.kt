package com.dicoding.storysub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.repository.StoryRepository
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.data.response.Story

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    fun getStoryDetail(id: String) = repository.getDetailStory(id)

    fun getDetail(id: String) {
        viewModelScope.launch {
            repository.getDetailStory(id).observeForever { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val story = response.data.story
                        _story.postValue(story!!)
                    }

                    is ApiResponse.Loading -> {
                        Log.d(TAG, "Loading")
                    }

                    is ApiResponse.Error -> {
                        Log.e(TAG, "Error")
                    }
                }
            }
        }
    }

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}