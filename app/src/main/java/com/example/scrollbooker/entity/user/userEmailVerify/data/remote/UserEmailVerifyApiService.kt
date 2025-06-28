package com.example.scrollbooker.entity.user.userEmailVerify.data.remote
import retrofit2.http.POST

interface UserEmailVerifyApiService {

    @POST("auth/verify-email")
    suspend fun verifyEmail()
}