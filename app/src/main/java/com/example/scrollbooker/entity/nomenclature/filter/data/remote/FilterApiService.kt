package com.example.scrollbooker.entity.nomenclature.filter.data.remote
import retrofit2.http.GET
import retrofit2.http.Path

interface FilterApiService {
    @GET("business-types/{businessTypeId}/filters")
    suspend fun getFiltersByBusinessType(
        @Path("businessTypeId") businessTypeId: Int,
    ): List<FilterDto>

    @GET("services/{serviceId}/filters")
    suspend fun getFiltersByService(
        @Path("serviceId") serviceId: Int,
    ): List<FilterDto>
}