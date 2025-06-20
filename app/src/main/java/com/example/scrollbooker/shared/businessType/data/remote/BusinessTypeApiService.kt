package com.example.scrollbooker.shared.businessType.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BusinessTypeApiService {
    @GET("/business-types")
    suspend fun getAllBusinessTypes(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<BusinessTypeDto>
}