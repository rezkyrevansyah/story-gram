package com.dicoding.storysub.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storysub.data.repository.StoryRepository
import com.dicoding.storysub.di.StoryInjection
import com.dicoding.storysub.viewmodel.AddViewModel
import com.dicoding.storysub.viewmodel.DetailViewModel
import com.dicoding.storysub.viewmodel.MainViewModel

class StoryViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    createMainViewModel() as T
                }

                modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                    createDetailViewModel() as T
                }

                modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                    createAddViewModel() as T
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun createMainViewModel(): MainViewModel {
        return MainViewModel(repository)
    }

    private fun createDetailViewModel(): DetailViewModel {
        return DetailViewModel(repository)
    }

    private fun createAddViewModel(): AddViewModel {
        return AddViewModel(repository)
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: createFactory(context).also { instance = it }
            }
        }

        private fun createFactory(context: Context): StoryViewModelFactory {
            return StoryViewModelFactory(StoryInjection.provideStoryRepository(context))
        }
    }
}
