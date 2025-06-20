package com.example.scrollbooker.shared.businessType.data.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypeRepository {
    fun getAllBusinessTypes(): Flow<PagingData<BusinessType>>
}