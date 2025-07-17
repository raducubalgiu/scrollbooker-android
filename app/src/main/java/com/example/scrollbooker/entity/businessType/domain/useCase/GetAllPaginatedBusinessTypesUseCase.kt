package com.example.scrollbooker.entity.businessType.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.businessType.domain.repository.BusinessTypeRepository
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

class GetAllPaginatedBusinessTypesUseCase(
    private val repository: BusinessTypeRepository
) {
    operator fun invoke(): Flow<PagingData<BusinessType>> {
        return repository.getAllPaginatedBusinessTypes()
    }
}