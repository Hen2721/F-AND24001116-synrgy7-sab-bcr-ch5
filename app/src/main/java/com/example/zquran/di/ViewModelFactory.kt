package com.example.zquran.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zquran.presentation.viewmodel.HomeViewModel
import com.example.zquran.presentation.viewmodel.LoginViewModel
import com.example.zquran.presentation.viewmodel.ProfileViewModel
import com.example.zquran.presentation.viewmodel.RegisterViewModel
import com.example.zquran.presentation.viewmodel.SurahDetailViewModel

class ViewModelFactory(private val provider: Provider) : ViewModelProvider.Factory {

    companion object {

        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        fun getInstance(provider: Provider) = synchronized(ViewModelFactory::class.java) {
            INSTANCE ?: ViewModelFactory(provider).also { INSTANCE = it }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(
                guestRepository = provider.remoteRepository,
                localRepository = provider.localRepository
            )
            RegisterViewModel::class.java -> RegisterViewModel(
                guestRepository = provider.remoteRepository,
                localRepository = provider.localRepository
            )
            HomeViewModel::class.java -> HomeViewModel(
                mainRepository = provider.remoteRepository,
                localRepository = provider.localRepository
            )
            SurahDetailViewModel::class.java -> SurahDetailViewModel(
                mainRepository = provider.remoteRepository,
            )
            ProfileViewModel::class.java -> ProfileViewModel(
                mainRepository = provider.remoteRepository,
                localRepository = provider.localRepository
            )
            else -> throw UnsupportedOperationException()
        } as T
    }
}