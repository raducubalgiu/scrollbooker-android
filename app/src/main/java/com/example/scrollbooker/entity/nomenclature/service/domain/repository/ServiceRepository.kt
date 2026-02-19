package com.example.scrollbooker.entity.nomenclature.service.domain.repository

import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters

interface ServiceRepository {
    suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>>
    suspend fun getServicesByServiceDomain(serviceDomainId: Int): Result<List<ServiceWithFilters>>
}