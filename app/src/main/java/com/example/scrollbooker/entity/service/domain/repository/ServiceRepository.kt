package com.example.scrollbooker.entity.service.domain.repository

import com.example.scrollbooker.entity.service.domain.model.Service

interface ServiceRepository {
    suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>>
    suspend fun getServicesByBusinessType(businessTypeId: Int): Result<List<Service>>
}