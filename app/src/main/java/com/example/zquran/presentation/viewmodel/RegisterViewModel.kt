package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zquran.data.local.LocalRepository
import com.example.zquran.domain.Surah
import com.example.zquran.domain.User
import com.example.zquran.domain.repository.GuestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val guestRepository: GuestRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _register: MutableLiveData<String> = MutableLiveData()
    val register: LiveData<String> = _register

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun register(user: User) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localRepository.validateRegisterInput(user)) {
                runCatching {
                    guestRepository.register(user)
                }.onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = exception.message
                    }
                }.onSuccess { value ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _register.value = value
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
}