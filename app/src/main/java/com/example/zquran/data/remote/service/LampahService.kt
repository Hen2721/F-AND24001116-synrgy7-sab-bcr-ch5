package com.example.zquran.data.remote.service

import com.example.zquran.data.remote.response.EditProfileResponse
import com.example.zquran.data.remote.response.LoginResponse
import com.example.zquran.data.remote.response.ProfileResponse
import com.example.zquran.data.remote.response.RegisterResponse
import com.example.zquran.domain.Login
import com.example.zquran.domain.Profile
import com.example.zquran.domain.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface LampahService {
    @POST("auth/register")
    suspend fun register(@Body user: User): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body user: Login): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @PATCH("auth/me")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body user: Profile): EditProfileResponse
}