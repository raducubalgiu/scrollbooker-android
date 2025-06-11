package com.example.scrollbooker.feature.services.domain.repository

import com.example.scrollbooker.feature.services.domain.model.Service

interface ServiceRepository {
    suspend fun getServices(userId: Int): List<Service>
    suspend fun getServicesByBusinessType(businessTypeId: Int): List<Service>
    suspend fun attachManyService(businessId: Int, serviceIds: List<Int>)
    suspend fun detachService(businessId: Int, serviceId: Int)
}