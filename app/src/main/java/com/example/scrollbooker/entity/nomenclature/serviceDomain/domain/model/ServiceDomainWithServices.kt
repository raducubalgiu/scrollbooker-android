package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model

data class ServiceDomainWithServices(
    val id: Int,
    val name: String,
    val services: List<ServiceDomainService>
)

data class ServiceDomainService(
    val id: Int,
    val name: String,
    val shortName: String,
    val isSelected: Boolean
)