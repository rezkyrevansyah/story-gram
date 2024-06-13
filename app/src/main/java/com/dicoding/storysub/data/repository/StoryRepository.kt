package com.dicoding.storysub.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import com.dicoding.storysub.data.api.StoryApiConfig
import com.dicoding.storysub.data.api.StoryApiService
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.paging.StoryPaging
import com.dicoding.storysub.data.preferences.UserPreference
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.data.response.DetailStoryResponse
import com.dicoding.storysub.data.response.FileUploadResponse
import com.dicoding.storysub.data.response.ListStoryItem
import com.dicoding.storysub.data.response.LoginResponse
import com.dicoding.storysub.data.response.StoryResponse
import com.dicoding.storysub.utils.reduceFileImage
import java.io.File

class StoryRepository(
    private val userPreference: UserPreference,
    private val apiService: StoryApiService
) {

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories(): LiveData<ApiResponse<StoryResponse>> = liveData {
        val userSession = userPreference.getSession().firstOrNull()
        if (userSession != null && !userSession.token.isNullOrEmpty()) {
            val token = userSession.token
            val apiService = StoryApiConfig.getApiService(token)
            emit(ApiResponse.Loading)
            try {
                val response = apiService.getStories()
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(ApiResponse.Error(errorMessage))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        } else {
            emit(ApiResponse.Error("Error"))
        }
    }

    suspend fun getStoriesLocation(): StoryResponse {
        return apiService.getStoriesWithLocation(location = 1)
    }

    fun getDetailStory(id: String): LiveData<ApiResponse<DetailStoryResponse>> = liveData {
        val userSession = userPreference.getSession().firstOrNull()
        if (userSession != null && !userSession.token.isNullOrEmpty()) {
            val token = userSession.token
            val apiService = StoryApiConfig.getApiService(token)
            emit(ApiResponse.Loading)
            try {
                val response = apiService.getDetailStory(id)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(ApiResponse.Error(errorMessage))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        } else {
            emit(ApiResponse.Error("Error"))
        }
    }

    fun uploadStory(file: File, description: String): LiveData<ApiResponse<FileUploadResponse>> =
        liveData {
            val userSession = userPreference.getSession().firstOrNull()
            if (userSession != null && !userSession.token.isNullOrEmpty()) {
                val token = userSession.token
                val apiService = StoryApiConfig.getApiService(token)
                emit(ApiResponse.Loading)
                try {
                    val imageFile = reduceFileImage(file)

                    val descriptionBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )
                    val response = apiService.uploadStory(multipartBody, descriptionBody)
                    emit(ApiResponse.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                    val errorMessage = errorBody.message
                    emit(ApiResponse.Error(errorMessage))
                } catch (e: Exception) {
                    emit(ApiResponse.Error(e.message.toString()))
                }
            } else {
                emit(ApiResponse.Error("Error"))
            }
        }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPaging(apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: StoryApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }

}