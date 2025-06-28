package com.example.scrollbooker.entity.user.userEmailVerify.domain.repository

interface UserEmailVerifyRepository {
    suspend fun verifyUserEmail()
}