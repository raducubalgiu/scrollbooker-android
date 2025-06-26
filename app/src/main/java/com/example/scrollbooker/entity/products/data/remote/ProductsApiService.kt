package com.example.scrollbooker.entity.products.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("users/{userId}/services/{serviceId}/products/")
    suspend fun getUserProducts(
        @Path("userId") userId: Int,
        @Path("serviceId") serviceId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<ProductDto>

    @GET("products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Int
    ): ProductDto
}