package com.example.scrollbooker.feature.myBusiness.domain.repository

import com.example.scrollbooker.feature.myBusiness.domain.model.Service
import retrofit2.http.Path

interface ServiceRepository {
    suspend fun getServices(userId: Int): List<Service>
}