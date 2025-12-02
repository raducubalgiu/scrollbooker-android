package com.example.scrollbooker.entity.booking.business.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkersRequest
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

class GetBusinessesSheetUseCase(
    private val repository: BusinessRepository
) {
    operator fun invoke(request: BusinessMarkersRequest): Flow<PagingData<BusinessSheet>> {
        return repository.getBusinessesSheet(request)
    }
}