package com.example.scrollbooker.entity.booking.products.data.remote
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("appointments/{appointmentId}/products")
    suspend fun getProductsByAppointmentId(
        @Path("appointmentId") appointmentId: Int
    ): List<ProductDto>

    @GET("posts/{postId}/products")
    suspend fun getPostLinkedProducts(
        @Path("postId") postId: Int
    ): List<ProductDto>

    @GET("products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Int
    ): ProductDto

    @POST("products")
    suspend fun createProduct(
        @Body request: ProductCreateRequestDto
    ): ProductDto

    @PUT("products/{productId}")
    suspend fun updateProduct(
        @Path("productId") productId: Int,
        @Body request: ProductCreateRequestDto
    ): ProductDto

    @DELETE("products/{productId}")
    suspend fun deleteProduct(
        @Path("productId") productId: Int
    )
}