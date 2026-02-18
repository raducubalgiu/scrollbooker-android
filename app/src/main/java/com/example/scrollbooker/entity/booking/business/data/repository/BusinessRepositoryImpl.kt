package com.example.scrollbooker.entity.booking.business.data.repository

import android.content.Context
import android.net.Uri
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
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
        val tempFiles = mutableListOf<File>()

        try {
            val parts = photos.filterNotNull().map { uri ->
                val (part, file) = uriToMultipartPart(context, uri)
                tempFiles += file
                part
            }

            return apiService.createBusiness(
                description = description.toNullableTextBody(),
                placeId = placeId.toTextBody(),
                businessTypeId = businessTypeId.toTextBody(),
                ownerFullName = ownerFullName.toTextBody(),
                photos = parts
            ).toDomain()
        } finally {
            tempFiles.forEach { runCatching { it.delete() } }
        }
    }

    override suspend fun updateBusinessGallery(
        businessId: Int,
        photos: List<Uri?>
    ) = withContext(Dispatchers.IO) {

        val tempFiles = mutableListOf<File>()

        try {
            val parts = photos.filterNotNull().map { uri ->
                val (part, file) = uriToMultipartPart(context, uri)
                tempFiles += file
                part
            }

            apiService.updateBusinessGallery(
                businessId = businessId,
                photos = parts
            )
        } finally {
            tempFiles.forEach { runCatching { it.delete() } }
        }
    }

    override suspend fun getStaticMap(
        lat: Double,
        lng: Double,
        zoom: Int
    ): Result<ByteArray> {
        return try {
            val response = apiService.getBusinessStaticMap(lat, lng, zoom)

            if (response.isSuccessful) {
                val bytes = response.body()?.bytes()
                if (bytes != null) {
                    Result.success(bytes)
                } else {
                    Result.failure(Exception("Empty body"))
                }
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }

    override suspend fun updateBusinessServices(serviceIds: List<Int>): List<ServiceDomainWithServices> {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.updateBusinessServices(request).map { it.toDomain() }
    }

    override suspend fun getBusiness(userId: Int): Business {
        return apiService.getBusinessByUserId(userId).toDomain()
    }

    override suspend fun getBusinessProfileById(businessId: Int): BusinessProfile {
        return apiService.getBusinessProfileById(businessId).toDomain()
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
    uri: Uri
): Pair<MultipartBody.Part, File> {

    val resolver = context.contentResolver
    val mime = resolver.getType(uri) ?: "image/jpeg"

    val tempFile = File.createTempFile("photo_", ".jpg", context.cacheDir)

    resolver.openInputStream(uri)?.use { input ->
        tempFile.outputStream().use { output -> input.copyTo(output) }
    } ?: error("Cannot open input stream for uri=$uri")

    val body = tempFile.asRequestBody(mime.toMediaTypeOrNull())
    val part = MultipartBody.Part.createFormData("photos", tempFile.name, body)

    return part to tempFile
}

