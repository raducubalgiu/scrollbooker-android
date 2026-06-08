package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service

data class BusinessServicesWithProducts(
    val service: Service,
    val products: List<Product>
)