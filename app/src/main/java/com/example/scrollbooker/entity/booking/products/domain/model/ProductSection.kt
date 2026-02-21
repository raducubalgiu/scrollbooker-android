package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter

data class ProductSection (
    val subFilter: SubFilter?,
    val products: List<Product>
)