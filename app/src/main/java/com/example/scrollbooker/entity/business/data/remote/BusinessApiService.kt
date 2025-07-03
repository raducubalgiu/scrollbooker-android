package com.example.scrollbooker.entity.business.data.remote
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface BusinessApiService {
    @GET("/businesses/search/")
    suspend fun searchBusinessAddress(
        @Query("query") query: String
    ): List<BusinessAddressDto>

    @PUT("/businesses/update-services/")
    suspend fun updateBusinessServices(
        @Body request: BusinessServicesUpdateRequest
    ): AuthStateDto
}