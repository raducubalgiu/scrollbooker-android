package com.example.scrollbooker.entity.booking.businessClient.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessClientsApiService {
    @GET("businesses/{businessId}/clients")
    suspend fun getBusinessClients(
        @Path("businessId") businessId: Int,
        @Query("query") query: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): PaginatedResponseDto<BusinessClientDto>

    @POST("businesses/{businessId}/clients")
    suspend fun createBusinessClient(
        @Path("businessId") businessId: Int,
        @Body request: BusinessClientCreateDto,
    ): BusinessClientDto
}
