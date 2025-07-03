package com.example.scrollbooker.entity.business.data.remote
import retrofit2.http.GET
import retrofit2.http.Query

interface BusinessApiService {
    @GET("/businesses/search")
    suspend fun searchBusinessAddress(
        @Query("query") query: String
    ): List<BusinessAddressDto>
}