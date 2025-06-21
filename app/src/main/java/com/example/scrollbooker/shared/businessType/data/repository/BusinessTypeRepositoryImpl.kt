package com.example.scrollbooker.shared.businessType.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.businessType.data.remote.BusinessTypeApiService
import com.example.scrollbooker.shared.businessType.data.remote.BusinessTypePagingSource
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType
import com.example.scrollbooker.shared.businessType.domain.repository.BusinessTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BusinessTypeRepositoryImpl @Inject constructor(
    private val apiService: BusinessTypeApiService
): BusinessTypeRepository {
    override fun getAllBusinessTypes(): List<BusinessType> {
        TODO("Not yet implemented")
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