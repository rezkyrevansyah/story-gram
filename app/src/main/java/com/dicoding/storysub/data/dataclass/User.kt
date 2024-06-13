package com.dicoding.storysub.data.dataclass

data class User(

    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)
