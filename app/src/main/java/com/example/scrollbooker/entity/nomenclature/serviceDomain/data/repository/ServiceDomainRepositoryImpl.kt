package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.repository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainApiService
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import javax.inject.Inject

class ServiceDomainRepositoryImpl @Inject constructor(
    private val apiService: ServiceDomainApiService
): ServiceDomainRepository {
    override suspend fun getServiceDomainsByBusinessDomain(businessDomainId: Int): List<ServiceDomain> {
        return apiService.getAllServiceDomainsByBusinessDomain(businessDomainId).map { it.toDomain() }
    }
}