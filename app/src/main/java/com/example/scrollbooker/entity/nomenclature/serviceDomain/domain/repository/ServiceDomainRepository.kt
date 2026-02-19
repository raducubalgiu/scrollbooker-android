package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

interface ServiceDomainRepository {
    suspend fun getServiceDomainsByBusinessDomain(
        businessDomainId: Int
    ): List<ServiceDomain>
    suspend fun getServiceDomainsWithServicesByBusinessDomainId(
        businessId: Int
    ): List<SelectedServiceDomainsWithServices>
}