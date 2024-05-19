package com.example.zquran.domain.repository

import com.example.zquran.data.remote.response.LoginResponse
import com.example.zquran.domain.Login
import com.example.zquran.domain.User

interface GuestRepository {
    suspend fun register(user: User): String
    suspend fun login(user: Login): LoginResponse
}