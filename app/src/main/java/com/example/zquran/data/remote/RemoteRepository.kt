package com.example.zquran.data.remote

import com.example.zquran.data.remote.response.EditProfileResponse
import com.example.zquran.data.remote.response.LoginResponse
import com.example.zquran.data.remote.response.toProfile
import com.example.zquran.data.remote.response.toSurah
import com.example.zquran.data.remote.response.toSurahDetail
import com.example.zquran.data.remote.service.LampahService
import com.example.zquran.data.remote.service.QuranService
import com.example.zquran.domain.Login
import com.example.zquran.domain.Profile
import com.example.zquran.domain.Surah
import com.example.zquran.domain.SurahDetail
import com.example.zquran.domain.User
import com.example.zquran.domain.repository.GuestRepository
import com.example.zquran.domain.repository.MainRepository

class RemoteRepository(
    private val quranService: QuranService,
    private val lampahService: LampahService
): MainRepository, GuestRepository {
    override suspend fun fetchAllSurahData(): List<Surah> {
        return quranService.getAllSurah().data?.map { result -> result.toSurah() }.orEmpty()
    }

    override suspend fun fetchSurahDetail(noSurah: Int): SurahDetail {
        val surahDetailList = quranService.getSurahByNumber(noSurah).data?.map { result -> result.toSurahDetail() }.orEmpty()
        val surahDetailAr = surahDetailList.find { it.edition.language == "ar" }
        val surahDetailId = surahDetailList.find { it.edition.language == "id" }

        surahDetailId?.ayahs?.map { ayat ->
            surahDetailAr?.ayahs?.filter { it.number == ayat.number }?.forEach { it.translationText = ayat.text }
        }

        return surahDetailAr!!
    }

    override suspend fun fetchProfile(token: String): Profile {
        return lampahService.getProfile("Bearer $token").data.toProfile()
    }

    override suspend fun updateProfile(token: String, user: Profile): EditProfileResponse {
        return lampahService.updateProfile("Bearer $token", user)
    }

    override suspend fun register(user: User): String {
        return lampahService.register(user).message
    }

    override suspend fun login(user: Login): LoginResponse {
        return lampahService.login(user)
    }
}