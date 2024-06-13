package com.dicoding.storysub.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import retrofit2.HttpException
import com.dicoding.storysub.data.api.AuthApiService
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.preferences.UserPreference
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.data.response.LoginResponse
import com.dicoding.storysub.data.response.RegisterResponse

class AuthRepository(
    private val userPreference: UserPreference,
    private val apiService: AuthApiService
) {

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResponse>> = liveData {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(ApiResponse.Error(errorMessage))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<ApiResponse<LoginResponse>> = liveData {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(ApiResponse.Error(errorMessage))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }

}