package com.example.scrollbooker.entity.nomenclature.service.data.repository
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServicesApiService
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceDomainWithServices
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters
import com.example.scrollbooker.entity.nomenclature.service.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServicesApiService
): ServiceRepository {
    override suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>> = runCatching {
        api.getServicesByBusinessId(businessId).map { it.toDomain() }
    }
    override suspend fun getServicesByServiceDomain(serviceDomainId: Int): Result<List<ServiceWithFilters>> = runCatching {
        api.getServicesByServiceDomainId(serviceDomainId).map { it.toDomain() }
    }

    override suspend fun getServicesByUserId(
        userId: Int,
        onlyWithProducts: Boolean
    ): Result<List<ServiceDomainWithServices>> = runCatching {
        api.getServicesByUserId(userId, onlyWithProducts).map { it.toDomain() }
    }
}