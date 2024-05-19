package com.example.zquran.di

import android.content.Context
import com.example.zquran.data.local.DataStoreManager
import com.example.zquran.data.local.LocalRepository
import com.example.zquran.data.remote.RemoteRepository
import com.example.zquran.data.remote.service.LampahService
import com.example.zquran.data.remote.service.QuranService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Provider(
    private val context: Context,
) {
    private val quranRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.alquran.cloud/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val lampahRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://lampah-server.vercel.app/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val quranService: QuranService = quranRetrofit.create(QuranService::class.java)

    private val lampahService: LampahService = lampahRetrofit.create(LampahService::class.java)

    private val dataStoreManager = DataStoreManager(context)

    val localRepository: LocalRepository = LocalRepository(
        dataStoreManager = dataStoreManager,
    )

    val remoteRepository: RemoteRepository = RemoteRepository(
        quranService = quranService,
        lampahService = lampahService
    )
}