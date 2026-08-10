package com.example.scrollbooker.entity.booking.business.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData

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
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import androidx.core.graphics.scale
import com.example.scrollbooker.entity.booking.business.data.remote.UnapprovedBusinessPagingSource
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService,
    @ApplicationContext private val context: Context
): BusinessRepository {
    override suspend fun updateBusinessGallery(
        businessId: Int,
        photos: List<Uri?>
    ): Unit = processAndUploadPhotos(context, photos) { parts ->
        apiService.updateBusinessGallery(businessId, parts)
    }

    override fun getUnapprovedBusinesses(): Flow<PagingData<UnapprovedBusiness>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { UnapprovedBusinessPagingSource(apiService) }
        ).flow
    }

    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }

    override suspend fun updateBusinessServices(
        businessId: Int,
        serviceIds: List<Int>
    ): List<SelectedServiceDomainsWithServices> {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.updateBusinessServices(businessId, request).map { it.toDomain() }
    }

    override suspend fun getBusiness(userId: Int): Business {
        return apiService.getBusinessByUserId(userId).toDomain()
    }

    override suspend fun getBusinessProfileByOwnerUsername(ownerUsername: String): BusinessProfile {
        return apiService.getBusinessProfileByOwnerUsername(ownerUsername).toDomain()
    }

    override suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState {
        val request = BusinessHasEmployeesUpdateRequest(hasEmployees)

        return apiService.updateBusinessHasEmployees(request).toDomain()
    }

    override suspend fun approveBusiness(userId: Int) {
        return apiService.approveBusiness(userId)
    }

    override suspend fun getBusinessesMarkers(request: SearchBusinessRequest): List<BusinessMarker> {
        return apiService.getBusinessesMarkers(request).map { it.toDomain() }
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

private const val MAX_IMAGE_DIMENSION = 1920
private const val JPEG_QUALITY = 85

suspend fun <T> processAndUploadPhotos(
    context: Context,
    photos: List<Uri?>,
    uploadBlock: suspend (List<MultipartBody.Part>) -> T
): T = withContext(Dispatchers.IO) {
    val parts = photos.filterNotNull()
        .map { uri -> async { uriToMultipartPart(context, uri) } }
        .awaitAll()

    uploadBlock(parts)
}

private fun uriToMultipartPart(context: Context, uri: Uri): MultipartBody.Part {
    val resolver = context.contentResolver
    val originalMime = resolver.getType(uri) ?: "image/jpeg"

    val compressedBytes = compressImage(context, uri)

    val mediaType = "image/jpeg".toMediaTypeOrNull()
    val body: RequestBody = compressedBytes.toRequestBody(mediaType)

    val extension = if (originalMime == "image/png") "png" else "jpg"
    val fileName = "photo_${System.currentTimeMillis()}.$extension"

    return MultipartBody.Part.createFormData("photos", fileName, body)
}

private fun compressImage(context: Context, uri: Uri): ByteArray {
    val resolver = context.contentResolver

    val rawBytes = resolver.openInputStream(uri)?.use { it.readBytes() }
        ?: error("Cannot open input stream for uri=$uri")

    val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, boundsOptions)

    val sampleSize = calculateInSampleSize(
        boundsOptions.outWidth,
        boundsOptions.outHeight
    )

    val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    val bitmap = BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, decodeOptions)
        ?: error("Cannot decode bitmap for uri=$uri")

    return try {
        val scaled = scaleToMaxDimension(bitmap)
        ByteArrayOutputStream().use { output ->
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
            if (scaled !== bitmap) scaled.recycle()
            output.toByteArray()
        }
    } finally {
        bitmap.recycle()
    }
}

private fun calculateInSampleSize(width: Int, height: Int): Int {
    var sampleSize = 1
    var longestSide = maxOf(width, height)
    while (longestSide / (sampleSize * 2) >= MAX_IMAGE_DIMENSION) {
        sampleSize *= 2
    }
    return sampleSize
}

private fun scaleToMaxDimension(bitmap: Bitmap): Bitmap {
    val longestSide = maxOf(bitmap.width, bitmap.height)
    if (longestSide <= MAX_IMAGE_DIMENSION) return bitmap

    val scale = MAX_IMAGE_DIMENSION.toFloat() / longestSide
    val newWidth = (bitmap.width * scale).toInt().coerceAtLeast(1)
    val newHeight = (bitmap.height * scale).toInt().coerceAtLeast(1)

    return bitmap.scale(newWidth, newHeight)
}

