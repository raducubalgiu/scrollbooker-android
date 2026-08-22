package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model

import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service

data class ServiceDomain(
    val id: Int,
    val name: String,
    val description: String?,
    val url: String?,
    val thumbnailUrl: String?,
    val services: List<Service>? = emptyList()
)