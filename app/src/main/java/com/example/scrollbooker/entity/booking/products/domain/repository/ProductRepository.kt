package com.example.scrollbooker.entity.booking.products.domain.repository
import com.example.scrollbooker.entity.booking.products.data.remote.ProductBaseInfoUpdateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts

interface ProductRepository {
    suspend fun getProductsByBusinessIdAndEmployeeId(
        businessId: Int,
        employeeId: Int?,
        onlyServicesWithProducts: Boolean,
        productsLimitPerService: Int?
    ): UserProducts
    suspend fun getProductsByAppointmentId(appointmentId: Int): List<Product>
    suspend fun getPostLinkedProducts(postId: Int): List<Product>
    suspend fun getProduct(productId: Int): Product
    suspend fun createProduct(product: ProductCreateRequest, filters: List<ProductFilterRequest>): Product
    suspend fun deleteProduct(productId: Int)

    suspend fun updateProductBaseInfo(productId: Int, product: ProductBaseInfoUpdateRequest): Product
}