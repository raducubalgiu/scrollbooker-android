package com.example.scrollbooker.entity.nomenclature.service.domain.repository

import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees

interface ServiceRepository {
    suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>>
    suspend fun getServicesByBusinessType(businessTypeId: Int): Result<List<Service>>
    suspend fun getServicesByServiceDomain(serviceDomainId: Int): Result<List<Service>>
    suspend fun getServicesByUserId(userId: Int): Result<List<ServiceWithEmployees>>
}