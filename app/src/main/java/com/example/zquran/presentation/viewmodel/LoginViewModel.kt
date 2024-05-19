package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zquran.data.local.LocalRepository
import com.example.zquran.domain.Login
import com.example.zquran.domain.repository.GuestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val guestRepository: GuestRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _login: MutableLiveData<String> = MutableLiveData()
    val login: LiveData<String> = _login

    private val _authentication = MutableLiveData<String>()
    val authentication: LiveData<String> = _authentication

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun login(user: Login) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localRepository.validateInput(user.username, user.password)) {
                runCatching {
                    guestRepository.login(user)
                }.onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = exception.message
                    }
                }.onSuccess { value ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _login.value = value.message
                        _authentication.value = value.data.token
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = "Username atau password tidak valid!"
                    _loading.value = false
                }
            }
        }
    }

    fun setToken(token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            localRepository.setToken(token)
        }
    }

    fun checkAuth() {
        viewModelScope.launch(Dispatchers.Main) {
            _authentication.value = localRepository.getToken().first() ?: ""
        }
    }
}