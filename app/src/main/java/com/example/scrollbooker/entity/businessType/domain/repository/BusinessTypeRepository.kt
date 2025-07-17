package com.example.scrollbooker.entity.businessType.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypeRepository {
    suspend fun getAllBusinessTypes(): List<BusinessType>
    fun getAllPaginatedBusinessTypes(): Flow<PagingData<BusinessType>>
}