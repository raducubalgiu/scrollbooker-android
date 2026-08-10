package com.example.scrollbooker.entity.booking.business.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

class GetUnapprovedBusinessesUseCase(
    private val repository: BusinessRepository
) {
    operator fun invoke(): Flow<PagingData<UnapprovedBusiness>> {
        return repository.getUnapprovedBusinesses()
    }
}