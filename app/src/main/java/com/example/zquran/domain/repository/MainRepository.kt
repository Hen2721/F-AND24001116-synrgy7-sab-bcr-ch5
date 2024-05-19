package com.example.zquran.domain.repository

import com.example.zquran.data.remote.response.EditProfileResponse
import com.example.zquran.domain.Profile
import com.example.zquran.domain.Surah
import com.example.zquran.domain.SurahDetail

interface MainRepository {
    suspend fun fetchAllSurahData(): List<Surah>
    suspend fun fetchSurahDetail(noSurah: Int): SurahDetail
    suspend fun fetchProfile(token: String): Profile
    suspend fun updateProfile(token: String, user: Profile): EditProfileResponse
}