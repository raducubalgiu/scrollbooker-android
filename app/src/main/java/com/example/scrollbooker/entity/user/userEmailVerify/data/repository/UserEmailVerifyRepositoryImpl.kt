package com.example.scrollbooker.entity.user.userEmailVerify.data.repository

import com.example.scrollbooker.entity.user.userEmailVerify.data.remote.UserEmailVerifyApiService
import com.example.scrollbooker.entity.user.userEmailVerify.domain.repository.UserEmailVerifyRepository
import javax.inject.Inject

class UserEmailVerifyRepositoryImpl @Inject constructor(
    private val apiService: UserEmailVerifyApiService
): UserEmailVerifyRepository {
    override suspend fun verifyUserEmail() {
        apiService.verifyEmail()
    }
}