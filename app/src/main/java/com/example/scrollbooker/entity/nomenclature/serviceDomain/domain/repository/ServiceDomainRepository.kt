package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices

interface ServiceDomainRepository {
    suspend fun getServiceDomainsByBusinessDomain(businessDomainId: Int): List<ServiceDomain>
    suspend fun getSelectedServiceDomainsWithServicesByBusinessDomainId(businessId: Int): List<SelectedServiceDomainsWithServices>
    suspend fun getServiceDomainsWithServicesByUserId(
        userId: Int,
        onlyWithProducts: Boolean,
        onlyWithFilters: Boolean
    ): Result<List<ServiceDomainWithEmployeeServices>>
}