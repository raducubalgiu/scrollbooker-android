package com.example.scrollbooker.entity.nomenclature.filter.data.repository

import com.example.scrollbooker.entity.nomenclature.filter.data.mapper.toDomain
import com.example.scrollbooker.entity.nomenclature.filter.data.remote.FilterApiService
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.repository.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val apiService: FilterApiService
): FilterRepository {
    override suspend fun getFiltersByBusinessTypeId(businessTypeId: Int): List<Filter> {
        return apiService.getFiltersByBusinessType(businessTypeId).map { it.toDomain() }
    }

    override suspend fun getFiltersByServiceId(serviceId: Int): List<Filter> {
        return apiService.getFiltersByService(serviceId).map { it.toDomain() }
    }
}