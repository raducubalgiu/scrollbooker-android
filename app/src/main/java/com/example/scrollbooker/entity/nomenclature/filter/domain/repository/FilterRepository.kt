package com.example.scrollbooker.entity.nomenclature.filter.domain.repository

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter

interface FilterRepository {
    suspend fun getFiltersByBusinessTypeId(businessTypeId: Int): List<Filter>
}