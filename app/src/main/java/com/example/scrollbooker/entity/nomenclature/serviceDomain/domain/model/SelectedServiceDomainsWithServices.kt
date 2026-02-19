package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model

data class SelectedServiceDomainsWithServices(
    val id: Int,
    val name: String,
    val services: List<SelectedService>
)

data class SelectedService(
    val id: Int,
    val name: String,
    val shortName: String,
    val isSelected: Boolean
)