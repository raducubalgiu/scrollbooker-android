package com.example.scrollbooker.shared.businessType.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.businessType.data.repository.BusinessTypeRepository
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType
import kotlinx.coroutines.flow.Flow

class GetAllBusinessTypesUseCase(
    private val repository: BusinessTypeRepository
) {
    operator fun invoke(): Flow<PagingData<BusinessType>> {
        return repository.getAllBusinessTypes()
    }
}