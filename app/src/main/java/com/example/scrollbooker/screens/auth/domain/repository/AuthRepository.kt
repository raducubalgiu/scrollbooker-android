package com.example.scrollbooker.screens.auth.domain.repository

import com.example.scrollbooker.screens.auth.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun logout()
}