package com.example.scrollbooker.entity.nomenclature.businessType.domain.model

data class BusinessType(
    val id: Int,
    val name: String,
    val businessDomainId: Int,
    val plural: String,
    val url: String?,
    val thumbnailUrl: String?
)