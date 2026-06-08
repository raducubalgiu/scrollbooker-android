package com.example.scrollbooker.entity.booking.products.domain.repository
import com.example.scrollbooker.entity.booking.products.data.remote.AddProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate

interface ProductRepository {
    suspend fun getProductsByAppointmentId(appointmentId: Int): List<Product>
    suspend fun getPostLinkedProducts(postId: Int): List<Product>
    suspend fun getProduct(productId: Int): Product
    suspend fun createProduct(
        productCreate: ProductCreate,
        serviceDomainId: Int,
        filters: List<AddProductFilterRequest>
    ): Product
    suspend fun updateProduct(
        productCreate: ProductCreate,
        serviceDomainId: Int,
        filters: List<AddProductFilterRequest>,
        productId: Int
    ): Product
    suspend fun deleteProduct(productId: Int)
}