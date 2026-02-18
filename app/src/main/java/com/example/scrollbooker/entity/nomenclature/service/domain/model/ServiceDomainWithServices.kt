package com.example.scrollbooker.entity.nomenclature.service.domain.model
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

data class ServiceDomainWithServices(
    val serviceDomain: ServiceDomain,
    val services: List<ServicesWithEmployees>
)

data class ServicesWithEmployees(
    val service: Service,
    val subFilters: List<SubFilter>,
    val productsCount: Int,
    val employees: List<ServiceEmployee>
)

data class ServiceEmployee(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float
)