package com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.repository.BusinessTypeRepository
import kotlinx.coroutines.flow.Flow

class GetAllPaginatedBusinessTypesUseCase(
    private val repository: BusinessTypeRepository
) {
    operator fun invoke(): Flow<PagingData<BusinessType>> {
        return repository.getAllPaginatedBusinessTypes()
    }
}