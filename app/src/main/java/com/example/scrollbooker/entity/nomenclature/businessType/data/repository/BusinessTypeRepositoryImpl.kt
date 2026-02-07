package com.example.scrollbooker.entity.nomenclature.businessType.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.nomenclature.businessType.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.businessType.data.remote.BusinessTypeApiService
import com.example.scrollbooker.entity.nomenclature.businessType.data.remote.BusinessTypePagingSource
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.repository.BusinessTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BusinessTypeRepositoryImpl @Inject constructor(
    private val apiService: BusinessTypeApiService
): BusinessTypeRepository {
    override suspend fun getAllBusinessTypes(): List<BusinessType> {
        return apiService.getAllBusinessTypes().map { it.toDomain() }
    }

    override suspend fun getAllAvailableBusinessTypes(): List<BusinessType> {
        return apiService.getAllAvailableBusinessTypes().map { it.toDomain() }
    }

    override suspend fun getBusinessTypesByBusinessDomain(businessDomainId: Int): List<BusinessType> {
        return apiService.getAllBusinessTypesByBusinessDomain(businessDomainId).map { it.toDomain() }
    }

    override fun getAllPaginatedBusinessTypes(): Flow<PagingData<BusinessType>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { BusinessTypePagingSource(apiService) }
        ).flow
    }
}