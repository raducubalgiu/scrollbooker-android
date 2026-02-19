package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter

data class ServiceDomainWithEmployeeServices(
    val id: Int,
    val name: String,
    val services: List<ServiceWithEmployees>
)

data class ServiceWithEmployees(
    val id: Int,
    val name: String,
    val shortName: String,
    val filters: List<Filter>,
    val employees: List<ServiceEmployee>,
    val productsCount: Int
)

data class ServiceEmployee(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float
)
