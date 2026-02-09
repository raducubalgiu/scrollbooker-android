package com.example.scrollbooker.entity.nomenclature.service.data.repository
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServicesApiService
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceDomainWithServices
import com.example.scrollbooker.entity.nomenclature.service.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServicesApiService
): ServiceRepository {
    override suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>> = runCatching {
        api.getServicesByBusinessId(businessId).map { it.toDomain() }
    }

    override suspend fun getServicesByBusinessType(businessTypeId: Int): Result<List<Service>> = runCatching {
        api.getServicesByBusinessTypeId(businessTypeId).map { it.toDomain() }
    }

    override suspend fun getServicesByServiceDomain(serviceDomainId: Int): Result<List<Service>> = runCatching {
        api.getServicesByServiceDomainId(serviceDomainId).map { it.toDomain() }
    }

    override suspend fun getServicesByUserId(userId: Int): Result<List<ServiceDomainWithServices>> = runCatching {
        api.getServicesByUserId(userId).map { it.toDomain() }
    }
}