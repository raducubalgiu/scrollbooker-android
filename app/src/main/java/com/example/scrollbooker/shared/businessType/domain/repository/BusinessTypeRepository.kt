package com.example.scrollbooker.shared.businessType.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypeRepository {
    fun getAllBusinessTypes(): List<BusinessType>
    fun getAllPaginatedBusinessTypes(): Flow<PagingData<BusinessType>>
}