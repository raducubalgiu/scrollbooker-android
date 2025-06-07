package com.example.scrollbooker.feature.myBusiness.services.domain.repository

import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service

interface ServiceRepository {
    suspend fun getServices(userId: Int): List<Service>
}