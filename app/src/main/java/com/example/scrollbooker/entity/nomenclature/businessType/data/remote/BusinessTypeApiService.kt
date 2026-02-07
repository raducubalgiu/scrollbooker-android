package com.example.scrollbooker.entity.nomenclature.businessType.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessTypeApiService {
    @GET("/business-types")
    suspend fun getAllBusinessTypes(): List<BusinessTypeDto>

    @GET("/business-types/available")
    suspend fun getAllAvailableBusinessTypes(): List<BusinessTypeDto>

    @GET("/business-types")
    suspend fun getAllPaginatedBusinessTypes(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<BusinessTypeDto>

    @GET("/business-domains/{businessDomainId}/business-types")
    suspend fun getAllBusinessTypesByBusinessDomain(
        @Path("businessDomainId") businessDomainId: Int
    ): List<BusinessTypeDto>
}