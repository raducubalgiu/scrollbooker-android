package com.example.scrollbooker.entity.filter.data.remote
import retrofit2.http.GET
import retrofit2.http.Path

interface FilterApiService {
    @GET("business-types/{businessTypeId}/filters")
    suspend fun getFiltersByBusinessType(
        @Path("businessTypeId") businessTypeId: Int,
    ): List<FilterDto>
}