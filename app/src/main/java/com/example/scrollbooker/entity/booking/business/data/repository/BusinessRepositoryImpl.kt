package com.example.scrollbooker.entity.booking.business.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessCreateDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessHasEmployeesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessSheetPagingSource
import com.example.scrollbooker.entity.booking.business.data.remote.SearchBusinessRequest
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
): BusinessRepository {
    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }

    override suspend fun updateBusinessServices(serviceIds: List<Int>): List<Service> {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.updateBusinessServices(request).map { it.toDomain() }
    }

    override suspend fun getBusiness(userId: Int): Business {
        return apiService.getBusinessByUserId(userId).toDomain()
    }

    override suspend fun getBusinessById(businessId: Int): Business {
        return apiService.getBusinessById(businessId).toDomain()
    }

    override suspend fun getBusinessLocation(
        businessId: Int,
        userLat: Float?,
        userLng: Float?
    ): BusinessLocation {
        return apiService.getBusinessLocation(businessId, userLat, userLng).toDomain()
    }

    override suspend fun getRecommendedBusinesses(lng: Float?, lat: Float?, timezone: String): List<RecommendedBusiness> {
        return apiService.getRecommendedBusinesses(lng, lat, timezone).map { it.toDomain() }
    }

    override suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState {
        val request = BusinessHasEmployeesUpdateRequest(hasEmployees)

        return apiService.updateBusinessHasEmployees(request).toDomain()
    }

    override suspend fun createBusiness(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String
    ): BusinessCreateResponse {
        val request = BusinessCreateDto(
            description = description,
            placeId = placeId,
            businessTypeId = businessTypeId,
            ownerFullName = ownerFullName
        )
        return apiService.createBusiness(request).toDomain()
    }

    override suspend fun getBusinessesMarkers(request: SearchBusinessRequest): PaginatedResponseDto<BusinessMarker> {
        val response = apiService.getBusinessesMarkers(request)

        return PaginatedResponseDto<BusinessMarker>(
            count = response.count,
            results = response.results.map { it.toDomain() }
        )
    }

    override fun getBusinessesSheet(
        request: SearchBusinessRequest,
        onTotalCountChanged: (Int) -> Unit
    ): Flow<PagingData<BusinessSheet>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                BusinessSheetPagingSource(
                    api = apiService,
                    request = request,
                    onTotalCountChanged = onTotalCountChanged
                )
            }
        ).flow
    }
}