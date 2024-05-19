package com.example.zquran.domain.repository

import com.example.zquran.domain.Profile

interface EditProfileRepository {
    suspend fun validateProfileInput(user: Profile): Boolean
}