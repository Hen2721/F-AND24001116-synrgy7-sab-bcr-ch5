package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zquran.data.local.LocalRepository
import com.example.zquran.domain.Profile
import com.example.zquran.domain.Surah
import com.example.zquran.domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val mainRepository: MainRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _profileData: MutableLiveData<Profile> = MutableLiveData()
    val profileData: LiveData<Profile> = _profileData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _logoutLoading: MutableLiveData<Boolean> = MutableLiveData()
    val logoutLoading: LiveData<Boolean> = _logoutLoading

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun getProfile() {
        _dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mainRepository.fetchProfile(localRepository.getToken().first()!!)
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    _dataLoading.value = false
                    _error.value = exception.message
                }
            }.onSuccess { profile ->
                withContext(Dispatchers.Main) {
                    _dataLoading.value = false
                    _profileData.value = profile
                }
            }
        }
    }

    fun updateProfile(user: Profile) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localRepository.validateProfileInput(user)) {
                runCatching {
                    mainRepository.updateProfile(localRepository.getToken().first()!!, user)
                }.onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = exception.message
                    }
                }.onSuccess { profile ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _message.value = profile.message
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = "Field tidak valid!"
                    _loading.value = false
                }
            }
        }
    }

    fun logout() {
        _logoutLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            localRepository.clearToken()
            withContext(Dispatchers.Main) {
                _logoutLoading.value = false
            }
        }
    }
}