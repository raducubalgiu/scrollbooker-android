package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.repository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainApiService
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import javax.inject.Inject

class ServiceDomainRepositoryImpl @Inject constructor(
    private val apiService: ServiceDomainApiService
): ServiceDomainRepository {
    override suspend fun getServiceDomainsByBusinessDomain(
        businessDomainId: Int
    ): List<ServiceDomain> {
        return apiService.getAllServiceDomainsByBusinessDomain(businessDomainId).map { it.toDomain() }
    }

    override suspend fun getSelectedServiceDomainsWithServicesByBusinessDomainId(
        businessId: Int
    ): List<SelectedServiceDomainsWithServices> {
        return apiService.getSelectedServiceDomainsWithServicesByBusinessId(businessId).map { it.toDomain() }
    }

    override suspend fun getServiceDomainsWithServicesByUserId(
        userId: Int,
        onlyWithProducts: Boolean,
        withFilters: Boolean
    ): Result<List<ServiceDomainWithEmployeeServices>> = runCatching {
        apiService.getAllServiceDomainsWithServicesByUserId(userId, onlyWithProducts, withFilters).map { it.toDomain() }
    }
}