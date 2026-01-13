package com.example.scrollbooker.entity.booking.business.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.data.remote.SearchBusinessRequest
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import kotlinx.coroutines.flow.Flow

interface BusinessRepository {
    suspend fun searchBusinessAddress(query: String): List<BusinessAddress>
    suspend fun updateBusinessServices(serviceIds: List<Int>): List<Service>
    suspend fun getBusiness(userId: Int): Business
    suspend fun getBusinessById(businessId: Int): Business
    suspend fun getBusinessLocation(businessId: Int, userLat: Float?, userLng: Float?): BusinessLocation
    suspend fun getRecommendedBusinesses(lng: Float?, lat: Float?, timezone: String): List<RecommendedBusiness>
    suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState
    suspend fun createBusiness(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String,
        photos: List<Uri?>
    ): BusinessCreateResponse

    suspend fun getBusinessesMarkers(request: SearchBusinessRequest): PaginatedResponseDto<BusinessMarker>
    fun getBusinessesSheet(
        request: SearchBusinessRequest,
        onTotalCountChanged: (Int) -> Unit
    ): Flow<PagingData<BusinessSheet>>
}