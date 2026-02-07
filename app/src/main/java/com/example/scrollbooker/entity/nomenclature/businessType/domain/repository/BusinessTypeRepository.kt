package com.example.scrollbooker.entity.nomenclature.businessType.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypeRepository {
    suspend fun getAllBusinessTypes(): List<BusinessType>
    suspend fun getAllAvailableBusinessTypes(): List<BusinessType>
    suspend fun getBusinessTypesByBusinessDomain(businessDomainId: Int): List<BusinessType>
    fun getAllPaginatedBusinessTypes(): Flow<PagingData<BusinessType>>
}