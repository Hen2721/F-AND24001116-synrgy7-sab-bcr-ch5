package com.example.zquran.data.local

import com.example.zquran.domain.Profile
import com.example.zquran.domain.User
import com.example.zquran.domain.repository.EditProfileRepository
import com.example.zquran.domain.repository.HomeRepository
import com.example.zquran.domain.repository.LoginRepository
import com.example.zquran.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LocalRepository(
    private val dataStoreManager: DataStoreManager
): LoginRepository, RegisterRepository, HomeRepository, EditProfileRepository {
    override suspend fun validateInput(username: String, password: String): Boolean {
        return username.isNotEmpty()
                && username.isNotBlank()
                && password.isNotEmpty()
                && password.isNotBlank()
    }

    override suspend fun validateRegisterInput(user: User): Boolean {
        return user.name.isNotEmpty()
                && user.name.isNotBlank()
                && user.username.isNotEmpty()
                && user.username.isNotBlank()
                && user.email.isNotEmpty()
                && user.email.isNotBlank()
                && user.phone.isNotEmpty()
                && user.phone.isNotBlank()
                && user.password.isNotEmpty()
                && user.password.isNotBlank()
    }

    override suspend fun setToken(token: String) {
        dataStoreManager.setToken(token)
    }

    override suspend fun getToken(): Flow<String?> {
        return dataStoreManager.getToken()
    }

    override suspend fun clearToken() {
        dataStoreManager.clearToken()
    }

    override suspend fun checkAuth(): Flow<Boolean?> {
        return combine(dataStoreManager.getToken()) { token -> !token.isNullOrEmpty() && token.isNotEmpty() }
    }

    override suspend fun validateProfileInput(user: Profile): Boolean {
        return user.name.isNotEmpty()
                && user.name.isNotBlank()
                && user.username.isNotEmpty()
                && user.username.isNotBlank()
                && user.email.isNotEmpty()
                && user.email.isNotBlank()
                && user.phone.isNotEmpty()
                && user.phone.isNotBlank()
    }
}