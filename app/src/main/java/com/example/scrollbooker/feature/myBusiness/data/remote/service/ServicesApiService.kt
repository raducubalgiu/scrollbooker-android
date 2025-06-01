package com.example.scrollbooker.feature.myBusiness.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Path

interface ServicesApiService {
    @GET("users/{userId}/services")
    suspend fun getServices(
        @Path("userId") userId: Int
    ): List<ServiceDto>
}