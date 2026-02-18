package com.example.scrollbooker.entity.booking.business.data.remote
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainWithServicesDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessApiService {
    @GET("/places")
    suspend fun searchBusinessAddress(
        @Query("query") query: String
    ): List<BusinessAddressDto>

    @GET("/maps/static")
    suspend fun getBusinessStaticMap(
        @Query("center_lat") lat: Double,
        @Query("center_lng") lng: Double,
        @Query("zoom") zoom: Int,
    ): Response<ResponseBody>

    @GET("/businesses/{businessId}")
    suspend fun getBusinessById(
        @Path("businessId") businessId: Int
    ): BusinessDto

    @GET("/businesses/{businessId}/profile")
    suspend fun getBusinessProfileById(
        @Path("businessId") businessId: Int
    ): BusinessProfileDto

    @GET("/businesses/recommended")
    suspend fun getRecommendedBusinesses(
        @Query("lng") lng: Float?,
        @Query("lat") lat: Float?,
        @Query("timezone") timezone: String
    ): List<RecommendedBusinessDto>

    @GET("/users/{userId}/businesses")
    suspend fun getBusinessByUserId(
        @Path("userId") userId: Int
    ): BusinessDto

    @PUT("/businesses/update-services")
    suspend fun updateBusinessServices(
        @Body request: BusinessServicesUpdateRequest
    ): List<ServiceDomainWithServicesDto>

    @PATCH("/businesses/update-has-employees")
    suspend fun updateBusinessHasEmployees(
        @Body request: BusinessHasEmployeesUpdateRequest
    ): AuthStateDto

    @Multipart
    @POST("/businesses")
    suspend fun createBusiness(
        @Part("description") description: RequestBody?,
        @Part("place_id") placeId: RequestBody,
        @Part("business_type_id") businessTypeId: RequestBody,
        @Part("owner_fullname") ownerFullName: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): BusinessCreateResponseDto

    @Multipart
    @PATCH("/businesses/{businessId}/gallery")
    suspend fun updateBusinessGallery(
        @Path("businessId") businessId: Int,
        @Part photos: List<MultipartBody.Part>
    )

    @POST("/businesses/markers")
    suspend fun getBusinessesMarkers(
        @Body request: SearchBusinessRequest
    ): PaginatedResponseDto<BusinessMarkerDto>

    @POST("businesses/locations")
    suspend fun getBusinessesSheet(
        @Body request: SearchBusinessRequest,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<BusinessSheetDto>
}