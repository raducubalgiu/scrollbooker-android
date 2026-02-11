package com.example.scrollbooker.entity.nomenclature.service.domain.model

data class Service(
    val id: Int,
    val name: String,
    val shortName: String,
    val description: String?,
    val businessDomainId: Int
)