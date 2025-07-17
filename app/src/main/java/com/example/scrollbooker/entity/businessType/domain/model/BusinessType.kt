package com.example.scrollbooker.entity.businessType.domain.model

data class BusinessType(
    val id: Int,
    val name: String,
    val businessDomainId: Int,
    val plural: String
)