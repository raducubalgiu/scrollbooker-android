package com.example.scrollbooker.entity.booking.business.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessHasEmployeesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessSheetPagingSource
import com.example.scrollbooker.entity.booking.business.data.remote.SearchBusinessRequest
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.util.UUID
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService,
   @ApplicationContext private val context: Context
): BusinessRepository {
    override suspend fun createBusiness(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String,
        photos: List<Uri?>
    ): BusinessCreateResponse {
        val validPhotos = photos.filterNotNull()

        if(validPhotos.isEmpty()) {
            Timber.tag("Create Business").e("At least one business photo is required")
            throw IllegalStateException("At least one business photo is required")
        }

        val photoParts = validPhotos.map { uri -> uriToMultipartPart(context, uri) }

        return apiService.createBusiness(
            description = description.toNullableTextBody(),
            placeId = placeId.toTextBody(),
            businessTypeId = businessTypeId.toTextBody(),
            ownerFullName = ownerFullName.toTextBody(),
            photos = photoParts
        ).toDomain()
    }

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

    override suspend fun getBusinessProfileById(businessId: Int): BusinessProfile {
        return apiService.getBusinessProfileById(businessId).toDomain()
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

private val TEXT = "text/plain".toMediaType()

private fun String.toTextBody(): RequestBody = this.toRequestBody(TEXT)
private fun Int.toTextBody(): RequestBody = this.toString().toRequestBody(TEXT)
private fun String?.toNullableTextBody(): RequestBody? = this?.toTextBody()

private fun uriToMultipartPart(
    context: Context,
    uri: Uri,
): MultipartBody.Part {
    val resolver = context.contentResolver

    val mime = resolver.getType(uri) ?: "image/jpeg"
    val fileName = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { c ->
            val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (c.moveToFirst() && idx >= 0) c.getString(idx) else null
        } ?: "photo_${UUID.randomUUID()}.jpg"

    val cacheFile = File(context.cacheDir, fileName)
    resolver.openInputStream(uri).use { input ->
        requireNotNull(input) { "Cannot open input stream for uri=$uri" }
        cacheFile.outputStream().use { output -> input.copyTo(output) }
    }

    val body = cacheFile.asRequestBody(mime.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("photos", cacheFile.name, body)
}
