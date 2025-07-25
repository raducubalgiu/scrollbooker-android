package com.example.scrollbooker.entity.booking.business.data.remote
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessApiService {
    @GET("/businesses/search")
    suspend fun searchBusinessAddress(
        @Query("query") query: String
    ): List<BusinessAddressDto>

    @GET("/businesses/{businessId}")
    suspend fun getBusinessById(
        @Path("businessId") businessId: Int
    ): BusinessDto

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
    ): AuthStateDto

    @PATCH("/businesses/update-has-employees")
    suspend fun updateBusinessHasEmployees(
        @Body request: BusinessHasEmployeesUpdateRequest
    ): AuthStateDto

    @POST("/businesses")
    suspend fun createBusiness(
        @Body request: BusinessCreateDto
    ): BusinessCreateResponseDto
}