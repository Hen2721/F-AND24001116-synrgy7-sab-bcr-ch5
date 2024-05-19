package com.example.zquran.domain.repository

import com.example.zquran.domain.User

interface RegisterRepository {
    suspend fun validateRegisterInput(user: User): Boolean
}