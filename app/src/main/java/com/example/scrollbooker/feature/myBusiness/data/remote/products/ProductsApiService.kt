package com.example.scrollbooker.feature.myBusiness.data.remote.products

import com.example.scrollbooker.feature.myBusiness.domain.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("users/{userId}/products")
    suspend fun getUserProducts(
        @Path("userId") userId: Int,
        @Query("page") page: Int
    ): List<Product>
}